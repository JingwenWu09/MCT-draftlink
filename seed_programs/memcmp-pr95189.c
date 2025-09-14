#include <math.h>
#include <stdio.h>
#include <stdlib.h>

char a4[] = "\0abc";
char a8[] = "\0abcdefg";
char a16[] = "\0abcdefghijklmno";

int cmp4(void) {
  return __builtin_memcmp(a4, "\0\0\0\0", 4);
}

int cmp8(void) {
  return __builtin_memcmp(a8, "\0\0\0\0\0\0\0\0", 8);
}

int cmp16(void) {
  return __builtin_memcmp(a16, "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0", 16);
}

int main(void) {
  if (cmp4() < 1 || cmp8() < 1 || cmp16() < 1) {
    __builtin_abort();
  }
}
