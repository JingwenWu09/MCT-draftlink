#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <math.h>

__attribute__((noinline)) float foo() {
  return sin(12.4f);
}

__attribute__((noinline)) float bar() {
  return sin(12.4f);
}

int main() {
  foo();
  bar();

  return 0;
}
