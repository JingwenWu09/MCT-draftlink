#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
#if __SIZEOF_LONG_LONG__ == 8 && __SIZEOF_INT128__ == 16 && __CHAR_BIT__ == 8
  unsigned __int128 a = 5;
  __builtin_mul_overflow(0xffffffffffffffffULL, (unsigned long long)a, &a);
  if (a != ((unsigned __int128)4 << 64 | 0xfffffffffffffffb)) {
    __builtin_abort();
  }
#endif
  return 0;
}
