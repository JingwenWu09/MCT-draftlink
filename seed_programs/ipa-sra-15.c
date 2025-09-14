#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct S {
  float red;
  void *blue;
  int green;
};

volatile int g;

void __attribute__((noipa)) check(float r, int g, int g2) {
  if (r < 7.39 || r > 7.41 || g != 6 || g2 != 6) {
    __builtin_abort();
  }
  return;
}

int main(int argc, char **argv) {
  struct S si;

  si.red = 7.4;
  si.green = 6;
  si.blue = &si;
  struct S *s = &si;
  int rec = 1;
  int t = 0;
  if (rec) {
    check(s->red, s->green, s->green);
    t = t + t;
  }
  check(s->red, s->green, s->green);
  g = t + t;
  return 0;
}
