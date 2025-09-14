#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct trio {
  int a, b, c;
};

int bar(int i, struct trio t) {
  if (t.a == t.b || t.a == t.c) {
    abort();
  }
}

int foo(struct trio t, int i) {
  return bar(i, t);
}

main() {
  struct trio t = {1, 2, 3};

  foo(t, 4);
  exit(0);
}
