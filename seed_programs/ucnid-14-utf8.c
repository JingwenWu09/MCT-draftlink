#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern void exit(int);

int main(void) {
  struct \U000000c0 {
    int \U000000c1;
  } x;
  struct \U000000c0 *y = &x;
  y->\U000000c1 = 1;
  if (x.\U000000c1 != 1) {
    abort();
  }
  goto \U000000ff;
\U000000ff:;
  enum e { \U000000c2 = 4 };
  if (\U000000c2 != 4) {
    abort();
  }
  exit(0);
}
