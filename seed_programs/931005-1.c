#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct T {
  char x;
};

struct T f(struct T s1) {
  struct T s1a;
  s1a.x = s1.x;
  return s1a;
}

main() {
  struct T s1a, s1b;
  s1a.x = 100;
  s1b = f(s1a);
  if (s1b.x != 100) {
    abort();
  }
  exit(0);
}
