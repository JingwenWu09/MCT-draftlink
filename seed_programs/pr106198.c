#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int printf(const char *, ...);
long a;
int b;
volatile int c;
int main() {
  long e = a;
  int f = a;
	do {
		if (b > 0) {
		  printf("0");
		  continue ;
		}
		if (f) {
		  printf("%ld", (long)b);
			continue ;
		}else{
			break ;
		}
	} while(1);
  e >= b &&c;
  return 0;
}
