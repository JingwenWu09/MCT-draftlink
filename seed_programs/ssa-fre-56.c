#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int x = 1;
int __attribute__((noinline, noclone)) fn() {
  return x;
}
int (*f)();
int main() {
  int res;
  f = fn;
  x = 0;
  res = f();
  res += x;
  if (res != 0) {
    __builtin_abort();
  }
  return 0;
}
