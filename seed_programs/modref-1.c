#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__attribute__((noinline)) void a(char *ptr, char *ptr2) {
  (*ptr)++;
  (*ptr2)++;
}

__attribute__((noinline)) void b(char *ptr) {
  a(ptr + 1, &ptr[3]);
}

int main() {
  char c[4] = {0, 1, 0, 0};
  b(c);
  return c[0] + c[3];
}
