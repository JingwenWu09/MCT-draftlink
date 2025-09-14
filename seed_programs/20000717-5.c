#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct trio {
  int a, b, c;
};

int bar(int i, int j, int k, struct trio t) {
  if (t.a != 1 || t.b != 2 || t.c != 3 || i != 4 || j != 5 || k != 6) {
    abort();
  }
}

int foo(struct trio t, int i, int j, int k) {
  return bar(i, j, k, t);
}

main() {
  struct trio t = {1, 2, 3};

  foo(t, 4, 5, 6);
  exit(0);
}
