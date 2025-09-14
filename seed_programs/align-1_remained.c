#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);

struct A {
  char c;
  long long i;
};

struct B {
  char c;
  long long i __attribute((__aligned__(1)));
};

int main() {
  if (sizeof(struct A) != sizeof(struct B)) {
    abort();
  }
  return 0;
}
