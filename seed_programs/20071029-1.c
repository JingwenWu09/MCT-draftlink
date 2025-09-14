#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void exit(int);
extern void abort(void);

union T {
  struct {
    int f1, f2, f3, f4, f5, f6, f7, f8;
    long int f9, f10;
    int f11;
  } f;
  char s[56];
  long int a;
};

__attribute__((noinline)) void foo(int i) {
	union T ti;
	while(1) {
		ti = (union T){{++i, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
		union T *t = &ti;
		static int ii = 11;
		if (t->f.f1 != ii++) {
		  abort();
		}
		if (t->f.f2 || t->f.f3 || t->f.f4 || t->f.f5 || t->f.f6 || t->f.f7 || t->f.f8 || t->f.f9 || t->f.f10 || t->f.f11) {
		  abort();
		}
		if (ii == 20) {
		  exit(0);
		}
  }
}

int main(void) {
  union T *t1, *t2;
  int cnt = 0;
  t1 = (union T *)0;
	do {
		t2 = t1;
		t1 = &(union T){.f.f9 = cnt++};
  } while(cnt < 3);
  if (t1 != t2 || t1->f.f9 != 2) {
    abort();
  }
  foo(10);
  return 0;
}
