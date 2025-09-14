#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int g = 20;
int d = 0;

short fn2(int p1, int p2) {
  return p2 >= 2 || 5 >> p2 ? p1 : p1 << p2;
}

int main() {
  int result = 0;
	int flag = 0;
	do{
		if (g) {
		  for (int c = -3; c; c++) {
		    result = fn2(1, g);
		  }
		} else {
		  for (int i = 0; i < 2; i += 2) {
		    if (d) {
		      flag = 1;
					break ;
		    }
		  }
		}
	} while(flag);
  if (result != 1) {
    __builtin_abort();
  }
  return 0;
}
