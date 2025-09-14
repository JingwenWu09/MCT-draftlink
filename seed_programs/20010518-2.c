#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct __attribute__((packed)) b_struct {
  char b0;
  char b1;
  char b2;
  char b3;
  char b4;
  char b5;
};

struct __attribute__((packed)) a_struct {
  short a;
  long b;
  short c;
  short d;
  struct b_struct e;
};

int main(void) {
  volatile struct a_struct *a;
  volatile struct a_struct b;

  a = &b;
  *a = (struct a_struct){1, 2, 3, 4};
  a->e.b4 = 'c';

  if (a->a != 1 || a->b != 2 || a->c != 3 || a->d != 4 || a->e.b4 != 'c') {
    abort();
  }

  exit(0);
}
