#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void exit(int);
extern void abort(void);

void *p;

void f1(void) {
  int i = 0, j = -1, k = -1;

  (j = ++i), (void)(typeof((int(*)[(k = ++i)]) p)) p;
  if (j != 1 || k != 2 || i != 2) {
    abort();
  }
}

void f2(void) {
  int i = 0, j = -1, k = -1;

  (j = ++i), (void)(typeof(int(*)[(k = ++i)])) p;
  if (j != 1 || k != 2 || i != 2) {
    abort();
  }
}

void f3(void) {
  int i = 0, j = -1, k = -1;
  void *q;

  (j = ++i), (void)((typeof(1 + (int(*)[(k = ++i)]) p)) p);
  if (j != 1 || k != 2 || i != 2) {
    abort();
  }
}

int main(void) {
  f1();
  f2();
  f3();
  exit(0);
}
