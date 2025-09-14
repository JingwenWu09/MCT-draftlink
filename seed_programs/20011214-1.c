#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern void exit(int);

#define small __attribute__((mode(QI))) int
int main() {
  int x, y = 0x400;

  x = (small)y;
  if (sizeof(small) != sizeof(char)) {
    abort();
  }
  if (sizeof(x) == sizeof(char) && x == y) {
    abort();
  }
  return 0;
}
