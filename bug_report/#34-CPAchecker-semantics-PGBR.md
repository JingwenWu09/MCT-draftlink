# Bug #7 in CPAchecker was fixed as a C standard library related issue. It was exposed by a test case generated using probability-guided branch reordering transformation.

```
Me:
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

extern void abort(void);

struct S {
  unsigned char a, c, d[16];
};

union U {
  short a;
  short b;
};

void foo(struct S *x, struct S *y, int from_union) {
  int a, b;
  unsigned char c, *d, *e;

  b = from_union;
  d = x->d;
  e = y->d;
  a = 0;

  int b_1 = b;
  unsigned char c_1 = c;
  struct S * y_1;
  unsigned char * e_1 =  y_1->d;
  int a_1 = a;
  struct S * x_1;
  unsigned char * d_1 =  x_1->d;

  while (b) {
    if (b < 8) {
      c = 0xff << (8 - b);
      b = 0;
    } else {
      c = 0xff;
      b -= 8;
    }
    e[a] = d[a] & c;
    a++;
  }

  while(b_1){
    if (b_1 >= 8) {
      c_1 = 0xff;
      b_1 -= 8;
    } else {
      c_1 = 0xff << (8 - b_1);
      b_1 = 0;
    }
    e_1[a_1] = d_1[a_1] & c_1;
    a_1++;
  }
  assert(b == b_1) ;
  assert(a == a_1) ;
  assert(c == c_1) ;
}

int main(void) {
  union U u = {25};

  struct S x = {0, 0, {0xaa, 0xbb, 0xcc, 0xdd}};
  struct S y = {0, 0, {0}};

  foo(&x, &y, u.b);

  return 0;
}

In this example, the assert() functions are both true, which is confirmed by compiling with gcc and clang,
while CPAchecker gives the FALSE result in predicateAnalysis and kInduction which means the assertion is false.
In the Counterexample.txt, the value of u.b is 0. Please help me to explain why it happens, thanks.

Command line:
scripts/cpa.sh -predicateAnalysis -64 -preprocess -setprop cpa.predicate.ignoreIrrelevantVariables=false <file_name.c>
scripts/cpa.sh -kInduction -64 -preprocess -setprop cpa.predicate.ignoreIrrelevantVariables=false <file_name.c>

```
```
Developer:
Initialization of union does not store values for all members in predicate analysis.
This was fixed a few days ago in f2470ec3.
```

