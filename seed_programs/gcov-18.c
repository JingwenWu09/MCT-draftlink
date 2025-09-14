#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a = 0;

void foo() {
  a = 1;
}

void bar() {
  a++;
}

int main() {
  foo();
  while( a == 1){
		bar();
	}
}
