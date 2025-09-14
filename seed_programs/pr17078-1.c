#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);

void test(int *ptr) {
  int i = 1;
  if (0) {
    i = 0;
  } else {
    i = 1;
  }
	i = 0;
  *ptr = i;
}

int main() {
  int i = 1;
  test(&i);
  if (i) {
    abort();
  }
  return 0;
}
