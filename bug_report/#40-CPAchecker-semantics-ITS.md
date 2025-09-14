# Bug #13 in CPAchecker was fixed as a language semantics related issue. It was exposed by a test case generated using if-else chain to switch conversion transformation. 

```
Me:

#include <assert.h>
#include <stdlib.h>

struct A {
  int x;
};

struct B {
  struct A arrA[2];
};

int main(void) {
  struct B s = {{{1}}};
  struct B s1 = s;

  volatile int i;
  unsigned char *volatile cp;
  unsigned char d[32] = {0};

  unsigned char c[32] = {0};
  unsigned char *p = d + i;
  int j;
  for (j = 0; j < 30; j++) {
    int x = 0xff;
    int y = *++p;

    //storeGlobalVarStmt
    //renameUseVarStmt
    int j_1 = j;
    int x_1 = x;

    if (j == 1) {
      x ^= 2;
    } else if (j == 2) {
      x ^= 4;
    } else if (j == 25) {
      x ^= s.arrA[0].x;
    } else {
      ;
    }

    switch (j_1) {
    case 1:
      x_1 ^= 2;
      break;
    case 2:
      x_1 ^= 4;
      break;
    case 25:
      x_1 ^= s1.arrA[0].x;
      break;
    default:
      break;
    }

    assert(j == j_1);
    assert(x == x_1);

    c[j] = y | x;
    cp = p;
  }

  if (c[0] == 0xff && c[1] == 0xfd && c[2] == 0xfb &&
      c[3] == 0xff && c[4] == 0xff && c[25] == 0xfe &&
      cp == d + 30) {
    abort();
  }
  exit(0);
}

In this case, struct B has a structured array member struct A. The assert() function is true,
which is confirmed by compiling with gcc and clang,
while CPAchecker gives the FALSE result using the following commands which means the assertion is false.

Command:
1)scripts/cpa.sh -kInduction -preprocess -64 -setprop cpa.predicate.ignoreIrrelevantVariables=false test.c
2)scripts/cpa.sh -predicateAnalysis -preprocess -64 -setprop cpa.predicate.ignoreIrrelevantVariables=false test.c
3)scripts/cpa.sh -valueAnalysis -preprocess -64 -setprop cpa.predicate.ignoreIrrelevantVariables=false test.c
4)scripts/cpa.sh -default -preprocess -64 -setprop cpa.predicate.ignoreIrrelevantVariables=false test.c
OS: Ubuntu22.04
Whether there is wrong when initializing a struct variable that contains structured array members?

```
```
Developer:
I can reproduce this with CPAchecker 2.2, but not with the current development version.
So it seems we have fixed this already.
```

