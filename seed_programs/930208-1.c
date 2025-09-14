#include <math.h>
#include <stdio.h>
#include <stdlib.h>
union T {
  long l;
  struct {
    char b3, b2, b1, b0;
  } c;
};

f(union T u) {
  ++u.c.b0;
  ++u.c.b3;
  return (u.c.b1 != 2 || u.c.b2 != 2);
}

main() {
  union T u;
  u.c.b1 = 2;
  u.c.b2 = 2;
  u.c.b0 = ~0;
  u.c.b3 = ~0;
  if (f(u)) {
    abort();
  }
  exit(0);
}
