#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct S {
  int (*fptr)();
};

int foo() {
  return 0;
}

int main() {
  struct S v;

  v.fptr = foo;
  return v.fptr();
}
