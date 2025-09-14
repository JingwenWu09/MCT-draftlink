#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdio.h>

__attribute__((noinline)) int foo() {
  printf("Hello world.\n");
  return 0;
}

__attribute__((noinline)) int bar() {
  printf("Hello world.\n");
  return 0;
}

int main() {
  return foo() + bar();
}
