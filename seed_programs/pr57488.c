#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  int i, j, *pj = &j, **ppj = &pj;
  int x;
  short s, *ps = &s, k;
  unsigned short u, *pu = &u, **ppu = &pu;
  char c, *pc = &c;
  unsigned char v = 48;

  for (; i <= 3; i++) {
    for (; j; j--) {
      ;
    }
    int p = *pj;
    int *px = &x;
    p = k;
    *px = **ppu = i;
    *ppj = &p;
    if (**ppj) {
      *pj = p;
    }
    u ^= p;
  }

  for (k = 1; k >= 0; k--) {
    int l;
    int p = 0;
    int *px = &x;
    p = k;
    *px = **ppu = i;
    *ppj = &p;
    if (**ppj) {
      *pj = p;
    }

    for (l = 1; l < 5; l++) {
      int m;
      for (m = 6; m; m--) {
        v--;
        *ps = *pc;
      }
    }
  }

  if (v != 0) {
    abort();
  }
  return 0;
}
