#include <math.h>
#include <stdio.h>
#include <stdlib.h>

volatile int a, b = -1;
char buf[64];

#define FMT "%+03d%02d"
const char *volatile fmt = FMT;

int main() {
  int c = a;
  int d = b;
  if (c >= -35791395 && c < 35791394 && d >= -1 && d < __INT_MAX__) {

    int n1 = __builtin_sprintf(buf, FMT, c + 1, d + 1);
    if (n1 > 7)
      __builtin_abort();

    int n2 = __builtin_sprintf(buf, fmt, c + 1, d + 1);

    if (n1 != n2)
      __builtin_abort();
  }
  return 0;
}
