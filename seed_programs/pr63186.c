#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void *a;
int b, c, d;

void bar() {
  switch (c) {
  case 0:
  lab:
    __asm__("");
    return;
  default:
    break;
  }
  b = 0;
  d = 0;
  a = &&lab;
}

void foo() {
  bar();
}

int main() {
}
