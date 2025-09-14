#include <math.h>
#include <stdio.h>
#include <stdlib.h>

union U {
  const int a;
  unsigned b : 20;
};

static union U u = {0x12345678};

int main(void) {
#ifdef __BYTE_ORDER__
#if __BYTE_ORDER__ == __ORDER_LITTLE_ENDIAN__
  return u.b - 0x45678;
#else
  return u.b - 0x12345;
#endif
#endif
  return 0;
}
