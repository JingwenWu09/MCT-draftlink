#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static int a = 2;
int b[1], c, d;

int main() {
  int e = a, *f = &b[0];
  if (d) {
    for (e = 0; e < 1; e++) {
      ;
    }
  }

	if(!e){
		f = 0;
	}

  if (b < f) {
    __builtin_abort();
  }
  if (*f) {
    c++;
  }

  return 0;
}
