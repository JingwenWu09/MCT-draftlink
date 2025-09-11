# Bug #4 in CPAchecker was confirmed as a language semantics related issue. It was exposed by a test case generated using probability-guided branch reordering transformation. 

```
Me:

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

typedef struct S {
  int v;
} S;

typedef struct W {
  int (*f)(int, int);  // function pointer
  S *a;
  int n;
} W;

int add(int x, int y) { return x + y; }

int proc(W *w, int t) {
  int first = 1;
  int base = w->a[0].v;

  for (int i = 0; i < w->n; i++) {
    int cur = w->a[i].v;
    if (first) {
      first = 0;
    } else if (cur >= base) {
      return w->f(cur, base);
    }
    base = cur;
  }
  return t;
}

int proc_equal(W *w, int t) {
  int first = 1;
  int base = w->a[0].v;

  for (int i = 0; i < w->n; i++) {
    int cur = w->a[i].v;
	if (cur >= base) {
      return w->f(cur, base);
    } else if (first) {
      first = 0;
	}
    base = cur;
  }
  return t;
}

int main() {
  S arr[3] = {{2}, {1}, {5}};
  W w1 = {add, arr, 3};
  W w2 = {add, arr, 3};

  int r1 = proc(&w1, 0);
  int r2 = proc(&w2, 0);
  assert(r1 == r2);

  return 0;
}

In this example, the assert() functions is true, which is confirmed by compiling with gcc and clang.
The predicate analysis and k-induction of CPAchecker both give the TRUE result,
while the value analysis gives the UNKNOWN result.
Command line: scripts/cpa.sh -valueAnalysis -64 -preprocess filename.c
```
```
Developer:

The component that tracks function-pointers currently does not handle structs etc.
In simple cases like here it would be feasible to add support for it.
```

