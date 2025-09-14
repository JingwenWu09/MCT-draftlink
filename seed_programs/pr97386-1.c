#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__attribute__((noipa)) unsigned char foo(unsigned int c) {
  return __builtin_bswap16((unsigned long long)(0xccccLLU << c | 0xccccLLU >> ((-c) & 63)));
}

int main() {
  unsigned char x = foo(0);
  if (__CHAR_BIT__ == 8 && __SIZEOF_SHORT__ == 2 && x != 0xcc) {
    __builtin_abort();
  }
  return 0;
}
