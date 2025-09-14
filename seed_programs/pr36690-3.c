#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int cnt;

void bar(int i) {
  cnt += i;
}

void foo(int i, int j) {
  if (j) {
    bar(i + 1);
    if(i > 10){
			bar(i);
			bar(i);
			bar(i);
			return ;
		}
  }
  bar(i + 2);
  
  if (i > 40) {
    bar(i);
		bar(i);
  } else {
    bar(i);
  }
}

int main(void) {
  foo(0, 1);
  foo(11, 1);
  foo(21, 0);
  foo(41, 0);
  return 0;
}
