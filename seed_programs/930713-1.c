#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct T{
  char x;
};

struct T f(struct T s1) {
  struct T s1a;
  s1a.x = 17;
  return s1a;
}

main() {
  struct T s1a, s1b;
  s1a.x = 13;
  s1b = f(s1a);
  if (s1a.x != 13 || s1b.x != 17) {
    abort();
  }
  exit(0);
}
