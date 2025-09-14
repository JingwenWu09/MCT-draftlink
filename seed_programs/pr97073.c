#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef unsigned long long L;
union U {
  L i;
  struct T {
    unsigned k;
    L l;
  } j;
} u;

__attribute__((noinline, noclone)) void foo(L x) {
  u.j.l = u.i & x;
}

int main() {
  u.i = 5;
  foo(-1ULL);
  if (u.j.l != 5) {
    __builtin_abort();
  }
  return 0;
}
