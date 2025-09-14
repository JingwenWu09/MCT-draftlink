#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct T{
  int a;
  char b;
};

int h(struct T *);
struct T g(struct T);

#if COMPILER != 2
struct T g(struct T x) {
  if (x.a != 13 || x.b != 47) {
    abort();
  }
  x.a = 0;
  x.b = 1;
  if (x.a != 0 || x.b != 1) {
    abort();
  }
  return x;
}
#endif

#if COMPILER != 1
f() {
  struct T x;
  x.a = 13;
  x.b = 47;
  g(x);
  if (x.a != 13 || x.b != 47) {
    abort();
  }
  x = g(x);
  if (x.a != 0 || x.b != 1) {
    abort();
  }
}
#endif

#if COMPILER != 2
main() {
  f();
  exit(0);
}
#endif
