# Bug #19 in CPAchecker was confirmed as a pointer alias related issue. It was exposed by a test case generated using Probability-Guided Branch Reordering transformation. 

```
Me:

//Original Mutate (not reduced)

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

  // ====== storeGlobalVarStmt ======
  struct x a_1 = a;
  struct x b_1 = b;
  struct x *c_1 = &b_1;
  int res_1 = res;

  // ====== 原始分支顺序 ======
  if (a.x1 != 1) {
    res = 3;
  } else if (c->x3 != 5) {
    res = 2;
  } else {
    res = 1;
  }

  // ====== 重排后的分支顺序 (PGBR) ======
  if (a_1.x1 == 1 && c_1->x3 == 5) {
    res_1 = 1;
  } else if (a_1.x1 != 1) {
    res_1 = 3;
  } else {
    res_1 = 2;
  }

  // ====== 等价性验证 ======
  assert(a.x1 == a_1.x1);
  assert(a.x2 == a_1.x2);
  assert(a.x3 == a_1.x3);
  assert(b.x1 == b_1.x1);
  assert(b.x2 == b_1.x2);
  assert(b.x3 == b_1.x3);
  assert(res == res_1);

  exit(0);
}

int main() {
  foobar(1, 2, 3);
}
```
```
Me:

//The simplest form that trigger this bug:
struct x {
	unsigned x1 : 1;
	unsigned x2 : 2;
};

int main() {
	int x = 1, y = 2;
	
	struct x a = {x, y};
	struct x b = {x, y};
	struct x *c = &b;		// delete this line, then the predicateAnalysis result is TRUE.
	
	struct x a_1 = a;
	struct x b_1 = b;

	assert(a.x1 == a_1.x1) ;
	assert(a.x2 == a_1.x2) ;
	assert(b.x1 == b_1.x1) ;
	assert(b.x2 == b_1.x2) ;
	
	return 0;
}

In this case, the PredicateAnalysis gave the FALSE result with the message Condition "b.x2 == b_1.x2" failed
(more details are shown in Counterexample.1.core.txt).
However, after I deleted the statement struct x *c = &b;,
the predicateAnalysis result became TRUE. In addition, both the results for KInduction were TRUE.
It seems that when there is a pointer to a struct variable,
the PredicateAnalysis will be affected during the processing of the structure's members's values and give incorrect results,
but KInduction will not be affected by this.
What's the difference between PredicateAnalysis and KInduction when handling a pointer to a struct variable?

Counterexample.1.core.txt:
line 19:	N10 -{[(a.x1) == (a_1.x1)]}-> N11
	__CPAchecker_TMP_1 == 1;
	((unsigned int)(a.x1)) == 1U;
	((unsigned int)(a_1.x1)) == 1U;
line 19:	N14 -{[(a.x1) == (a_1.x1)]}-> N16
	((unsigned int)(a.x1)) == 1U;
	((unsigned int)(a_1.x1)) == 1U;
line 20:	N19 -{[(a.x2) == (a_1.x2)]}-> N20
	__CPAchecker_TMP_3 == 1;
	((unsigned int)(a.x2)) == 2U;
	((unsigned int)(a_1.x2)) == 2U;
line 20:	N23 -{[(a.x2) == (a_1.x2)]}-> N25
	((unsigned int)(a.x2)) == 2U;
	((unsigned int)(a_1.x2)) == 2U;
line 21:	N28 -{[(b.x1) == (b_1.x1)]}-> N29
	__CPAchecker_TMP_5 == 1;
	((unsigned int)(b.x1)) == 1U;
	((unsigned int)(b_1.x1)) == 1U;
line 21:	N32 -{[(b.x1) == (b_1.x1)]}-> N34
	((unsigned int)(b.x1)) == 1U;
	((unsigned int)(b_1.x1)) == 1U;
line 22:	N37 -{[(b.x2) == (b_1.x2)]}-> N38
	__CPAchecker_TMP_7 == 1;
	((unsigned int)(b.x2)) == 2U;
	((unsigned int)(b_1.x2)) == 0U;
line 22:	N41 -{[!((b.x2) == (b_1.x2))]}-> N44
	((unsigned int)(b.x2)) == 2U;
	((unsigned int)(b_1.x2)) == 0U;
line 22:	N44 -{__assert_fail("b.x2 == b_1.x2", "demo.c", 22, "__PRETTY_FUNCTION__");}-> N56

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
