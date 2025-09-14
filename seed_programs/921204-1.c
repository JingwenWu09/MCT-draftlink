#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#if __INT_MAX__ < 2147483647
int main(void) {
  exit(0);
}
#else
struct bf{
  unsigned b0 : 1, f1 : 17, b18 : 1, b19 : 1, b20 : 1, f2 : 11;
} bf;

union bu{
  struct bf b;
  unsigned w;
};

union bu f(union bu i) {
  union bu o = i;

  if (o.b.b0) {
    o.b.b18 = 1, o.b.b20 = 1;
  } else {
    o.b.b18 = 0, o.b.b20 = 0;
  }

  return o;
}

main() {
  union bu a;
  union bu r;

  a.w = 0x4000000;
  a.b.b0 = 0;
  r = f(a);
  if (a.w != r.w) {
    abort();
  }
  exit(0);
}
#endif
