#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);
extern void exit(int);

int foo(int);
int bar(int);

int (*ptr)(int);
int *f_addr;

int main() {
  ptr = bar;
  foo(7);
  exit(0);
}

int __attribute__((noinline)) bar(b) int b;
{
  if (f_addr == (int *)__builtin_return_address(0)) {
    return b;
  } else {
    exit(0);
  }
}

int __attribute__((noinline)) foo(f) int f;
{
  f_addr = (int *)__builtin_return_address(0);
  return (*ptr)(f);
}
