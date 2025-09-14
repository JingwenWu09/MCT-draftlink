# Bug #11 in SeaHorn was confirmed as a language semantics related issue. It was exposed by a test case generated using dead code elimination transformation.

```
Me

#include "seahorn/seahorn.h"

void dummy(int *x, int y) {
  // Do nothing (placeholder)
}

int main() {
  int number_columns = 9;
  int cnt0 = 0;
  int cnt1 = 0;
  int i, A;

  int x = 2147483647;
  int y = 2147483646;

  int number_columns1 = number_columns;
  int i1 = i; int x1= x; int y1=y;
  int A1 = A; int cnt01 = cnt0; int cnt11= cnt1;
  for (i = number_columns - 1; i != 0; i--) {
    if (i == 1) {
      if (i > 9)  abort();
      for (int j = 0; j < 1; j++) x++;
      dummy(&A, i);
      cnt0++;
    } else {
      if (i < 0) abort();
      if (i == 2) {
        for (int j = 0; j < 2; j++) y++;
      }
      dummy(&A, i - 1);
      cnt1++;
    }
  }

  for (i1 = number_columns1 - 1; i1 != 0; i1--) {
    if (i1 == 1) {
      for (int j = 0; j < 1; j++) x1++;
      dummy(&A1, i1);
      cnt01++;
    } else {
      if (i1 == 2) {
        for (int j = 0; j < 2; j++) y1++;
      }
      dummy(&A1, i1 - 1);
      cnt11++;
    }
  }

  sassert(cnt0 == cnt01);
  sassert(cnt1 == cnt11);

  sassert(x == x1); //sat
  sassert(y == y1); //sat
  sassert(x1 == -2147483648); //sat
  sassert(y1 == -2147483648); //sat

  return 0;
}

In this example, the verification result of each "sassert" statement is as stated in the comment.
Each assertion should be hold which is confirmed by gcc and clang,
while seahorn gives some unexpected results. 
```
```
Developer:

Again, these programs have signed integer overflows which is undefined behavior.
If LLVM can detect such cases then it can do whatever it wants with the code.
```



