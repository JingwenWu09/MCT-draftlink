# Bug #19 in CPAchecker was confirmed as a pointer alias related issue. It was exposed by a test case generated using Probability-Guided Branch Reordering transformation. 

```
Me:

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

struct x {
  unsigned x1 : 1;
  unsigned x2 : 2;
  unsigned x3 : 3;
};

void foobar(int x, int y, int z) {
  struct x a = {x, y, z};
  struct x b = {x, y, z};
  struct x *c = &b;
  int res = 0;
  c->x3 += (a.x2 - a.x1) * c->x2;

  struct x a_1 = a;
  struct x b_1 = b;
  struct x *c_1 = &b_1;
  int res_1 = res;

  if (a.x1 != 1) {
    res = 3;
  } else if (c->x3 != 5) {
    res = 2;
  } else {
    res = 1;
  }

  if (a_1.x1 == 1 && c_1->x3 == 5) {
    res_1 = 1;
  } else if (a_1.x1 != 1) {
    res_1 = 3;
  } else {
    res_1 = 2;
  }

  assert(a.x1 == a_1.x1);
  assert(a.x2 == a_1.x2);
  assert(a.x3 == a_1.x3);
  assert(b.x1 == b_1.x1);
  assert(b.x2 == b_1.x2); // false
  assert(b.x3 == b_1.x3);
  assert(res == res_1);

  exit(0);
}

int main() {
  foobar(1, 2, 3);
}

In this program, PredicateAnalysis reports a failure, with the counterexample indicating that b.x2 == b_1.x2 evaluates inconsistently. However, other analyses such as KInduction return TRUE.
We further observed that the presence of a pointer to a struct (e.g., struct x *c = &b;) is the key factor:
removing the pointer makes PredicateAnalysis succeed.
It seems that when there is a pointer to a struct variable,
the PredicateAnalysis will be affected during the processing of the structure's members's values and give incorrect results,
but KInduction will not be affected by this.
What's the difference between PredicateAnalysis and KInduction when handling a pointer to a struct variable?

command line:
./scripts/cpa.sh -predicateAnalysis -preprocess -64 -setprop cpa.predicate.ignoreIrrelevantVariables=false <filename.c>

```
```
Developer:

The option -setprop cpa.predicate.handleStringLiteralInitializer=true that you added to the command line
has no effect as it uses a wrong name, the correct name is cpa.predicate.handleStringLiteralInitializers.
But it is irrelevant to this program anyway.
k-Induction seems to prove it correctly because it is combined with a second analysis for generating invariants,
and that one finds the proof.
For -predicateAnalysis, it gives the correct result after removing
-setprop cpa.predicate.ignoreIrrelevantVariables=false (removing this makes it ignore variable c).
But this is just a coincidence because c is unused.

And the reason for the failure is that struct x has bit fields,
and we currently do not have proper support for pointers to such structs,
at least if there are fields that are not byte-aligned.
If x2 is turned into a regular field, the result is correct.
And if there is no pointer to b and it lives on the stack, we can also track it correctly.

```
