#include <math.h>
#include <stdio.h>
#include <stdlib.h>

short a = 0;
long b = 0;
char c = 0;
void d() {
  int e = 0;
f:
  for (a = 6; a;) {
    c = e;
  }
  e = 0;
  for (; e == 20; ++e) {
    for (; b;) {
      goto f;
    }
  }
}
int main() {
  return 0;
}
