#include <math.h>
#include <stdio.h>
#include <stdlib.h>


void baz() {
  return;
}

void bar(int *p) {
  *p = 1;
}

void foo(int a) {
	static int *pp;
  if (a == 2) {
    baz();
    return;
  }
  if (a > 1) {
    int x __attribute__((aligned(256)));
    pp = &x;
    bar(&x);
    if (!x) {
      baz();
    	return;
    }
  }
}

int main(int argc, char **argv) {
  foo(4);
  foo(3);

  return 0;
}
