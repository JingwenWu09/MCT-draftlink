#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int g_21;
int g_211;
int g_261;

static void __attribute__((noinline, noclone)) func_32(int b) {
  if (b) {
    g_21 = 1;
  }
	do{
		int flag = 1;
		for (g_261 = -1; g_261 > -2; g_261--) {
		  if (g_211 + 1) {
		    return;
		  } else {
		    g_21 = 1;
		    flag = 0;
				break ;
		  }
		}
		if(flag) {
			break ;
		}
	}while(1);
}

extern void abort(void);

int main(void) {
  func_32(0);
  if (g_261 != -1) {
    abort();
  }
  return 0;
}
