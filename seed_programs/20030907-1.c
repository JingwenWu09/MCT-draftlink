#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);
extern void exit(int);

int main() {
  int i;
  i = 2;
  switch (i) {
  case 1 ... 5:
    goto L1;
  default:
    abort();
    goto L1;
  }
L1:
  exit(0);
}
