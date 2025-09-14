#include <math.h>
#include <stdio.h>
#include <stdlib.h>
void assert2(a) {
  if (a != 1) {
    abort();
  }
}

int h1(int *p) {
  return *p & 255;
}

void p1() {
  int a = 0x01020304;
  assert2(h1(&a) == 0x04);
}

int h2(a) {
  return a > 0;
}

p2() {
  assert2(h2(1));
}

h3(int *p) {
  *p |= 255;
}

p3() {
  int temp;
  int *p = &temp;
  h3(p);
}

main() {
  p1();
  p2();
  p3();
  puts("Compiler test passed.");
}
