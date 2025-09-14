# Bug #8 in CPAchecker was confirmed as a C standard library related issue. It was exposed by a test case generated using dead code elimination transformation.

```
Me:

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

int a;
int *a_global_p = &a;
int c;
int *c_global_p = &c; 
__attribute__((noinline, noclone)) void foo(int x) {
  if (x == 0) {
    c++;
  }
}

int main(int argc, char *argv[]) {
  int j, k, b = 0;
  int c_store = *c_global_p;
  int a_store = *a_global_p;
  int argc_1 = argc;
  int b_1 = b;
  if (argc == 0) {
    b = 1;
  }
  for (j = 0; j < 3; j++) {
    for (k = 0; k < 1; k++) {
      foo(0);
      if (b) {
        for (k = -1; a;) {
          ;
        }
      }
    }
  }
  if (c != 3) {
    abort();
  }

  *c_global_p = c_store;
  *a_global_p = a_store;
  for (j = 0; j < 3; j++) {
    for (k = 0; k < 1; k++) {
      foo(0);
    }
  }
  assert(b == b_1) ;
  assert(argc == argc_1) ;
  return 0;
}


In this example, the assert() functions are both true, which is confirmed by compiling with gcc and clang, while CPAchecker gives the FALSE result which means the assertion `assert(argc == argc_1)` is false. Is there no way for CPAchecker to process the relevant information from the external input？

Command line:
scripts/cpa.sh -predicateAnalysis -64 -preprocess -setprop cpa.predicate.ignoreIrrelevantVariables=false <file_name.c>

```
```
Developer:

Your example has only one assert call, and it checks argc == 1,
but this is not guaranteed to be true. So the result happens to be correct.
But in fact we do not model argc and argv at all right now.
I wasn't requested so far, and I guess must users model input to the verification tasks
via calls to __VERIFIER_nondet_* instead of command-line arguments.
```

