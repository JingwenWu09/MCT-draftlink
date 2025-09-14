#include <math.h>
#include <stdio.h>
#include <stdlib.h>

unsigned char c;
void __attribute__((noipa)) e() {
  do {
  } while (++c);
}
int main() {
  e();
  if (c != 0) {
    __builtin_abort();
  }
  return 0;
}
