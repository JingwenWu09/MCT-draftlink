#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern int printf(__const char *__restrict __format, ...);

int x = 1;
int main() {
  asm goto("decl %0; jnz %l[a]" ::"m"(x) : "memory" : a);
  printf("Hello world\n");
a:
  return 0;
}
