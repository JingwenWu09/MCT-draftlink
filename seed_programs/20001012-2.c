#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern void exit(int);

void foo(char *x, const char *y, ...) {
}

double bar(const char *x, long y) {
  return 0.0;
}

typedef __SIZE_TYPE__ size_t;
extern size_t strlen(const char *);

int main() {
  const char *x = "";
  double returnValue;
  if (x[0] != '\0') {
    char y[6 + strlen(x)];
    foo(y, "FOO", x);
    returnValue = bar(y, 0);
    if (returnValue != 1.0) {
		  abort();
		}
		exit(0);
  }

  returnValue = (__extension__((union {
                   unsigned __l __attribute__((__mode__(__SI__)));
                   float __d;
                 }){.__l = 0x3f800000UL})
                     .__d);
  if (returnValue != 1.0) {
    abort();
  }
  exit(0);
}
