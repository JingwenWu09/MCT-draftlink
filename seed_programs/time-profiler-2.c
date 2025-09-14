#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <unistd.h>

__attribute__((noinline)) int foo() {
  return 1;
}

__attribute__((noinline)) int bar() {
  return 1;
}

__attribute__((noinline)) int baz() {
  return 1;
}

__attribute__((noinline)) int baz1() {
  return 1;
}

int main() {
  int f = fork();
  int r = 0;

  foo();

  if (f < 0) {
    return 1;
  }

  if (f == 0) {
    r = bar() - foo();
  } else {
    r = foo() - foo();
  }

  return r;
}
