#include <math.h>
#include <stdio.h>
#include <stdlib.h>
void abort();
void exit(int);

void test(int x, int y) {
  if (x == y) {
    abort();
  }
}

void foo(int x, int y) {
  if (x == y) {
    ;
  }
   
	if (x != y) {
		test(x, y);
  }

}

int main(void) {
  foo(0, 0);

  exit(0);
}
