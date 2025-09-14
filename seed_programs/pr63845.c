#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int __attribute__((noinline, noclone)) foo(void *p) {
  return *(int *)p;
}

int main() {
  char a = 0;
  foo(&a);
  return 0;
}
