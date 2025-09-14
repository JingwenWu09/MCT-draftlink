#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>

double gd[8];
int z;

double bar(double q) __attribute__((noinline));
double foo(double q) __attribute__((noinline));
int ck(double q) __attribute__((noinline));
int main(int argc, char **argv) __attribute__((no_split_stack));

double bar(double q) {
  double d[8];
  for (unsigned i = 0; i < 8; ++i) {
    d[i] = gd[8 - i - 1];
  }
  return q + d[z & 3];
}

double foo(double d) {
  return bar(d);
}

int ck(double d) {
  if (d != 64.0) {
    abort();
  }
  return 0;
}

typedef double (*fp)(double);
fp g = foo;

int main(int argc, char **argv) {
  return ck(g(64.0));
}
