#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);
extern void exit(int);

int main() {
	int *c;
	int a = 1, b = 2;
	c = &a;
  if (*c != 1 || b != 2) {
    abort();
  }
  exit(0);
}
