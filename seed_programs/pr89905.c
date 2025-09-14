#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void __attribute__((noinline)) optimize_me_not() {
  __asm__ volatile("" : : : "memory");
}
char c, d = 22, f;
short e, g;
int h;
char(a)() {
}
char(b)() {
  return 0;
}
void i() {
  char j;
  for (; h < 1;) {
    short k[9] = {1, 1, 1, 1, 1, 1, 1, 1, 1};
    int l, i = 5;
    short m[3] = {0, 0, 0};
    for (; h < 7; h++) {
      for (; d >= 33;) {
        ++k[8];
        f = (c || a()) && g;
      }
    }
    i++;
    j = b() || m[2];
    l = 0;
    for (; l <= 6; l = d) {
      e = k[8];
    }

    optimize_me_not();
  }
}
int main() {
  i();
}
