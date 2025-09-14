#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static int *p;
void bar(int cnt) {
  if (cnt == 0) {
    p = &cnt;
    bar(1);
    if (cnt != 1) {
      __builtin_abort();
    }
  } else if (cnt == 1) {
    *p = 1;
  }
}
int main() {
  bar(0);
  return 0;
}
