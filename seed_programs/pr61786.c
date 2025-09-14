#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a, *c = &a, d;
char b = 1;

void fn1() {
  d = 1;
	do{
		if (b == d) {
		  d = *c;
		}else{
			break ;
		}
	}while(b);
}

int fn2() {
  fn1();
  return 0;
}

int main() {
  fn2();
  return 0;
}
