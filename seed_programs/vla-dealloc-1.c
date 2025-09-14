#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#if (__SIZEOF_INT__ <= 2)
#define LIMIT 10000
#else
#define LIMIT 1000000
#endif

int main(void) {
	void *volatile p;
  int n = 0;
  if (0) {
  	;
  }
	do{
		int x[n % 1000 + 1];
		x[0] = 1;
		x[n % 1000] = 2;
		p = x;
		n++;
  }while (n < LIMIT);

  return 0;
}
