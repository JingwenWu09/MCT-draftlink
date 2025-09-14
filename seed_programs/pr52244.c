#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

union u_r {
  _Bool b;
  unsigned char c;
};

union u_r bar(void) {
  union u_r u;
  u.c = 0x12;
  return u;
}

union u_r __attribute__((noinline)) foo(void) {
  union u_r u;

  u.b = 1;
  u = bar();

  return u;
}

int main(int argc, char **argv) {
  union u_r u = foo();
  if (u.c != 0x12) {
    abort();
  }
  return 0;
}
