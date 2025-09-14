#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern void exit(int);

int main() {
  int x = 56;
  char *y = "abc";
  int returnValue;
  int a;
  char *b = y;
  int xx = x;
  char **yy = &y;
  if (xx != 56) {
    abort();
  }
  if (**yy != 'a') {
    abort();
  }
  *yy = "def";
  a = 1;
  if (a) {
    y = b;
    xx = x;
    yy = &y;
    if (xx != 56) {
      abort();
    }
    if (**yy != 'a') {
      abort();
    }
    a = 26;
  }
  if (a) {
    returnValue = a;
    if (returnValue != 26) {
		  abort();
		}
		exit(0);
  }

  xx = x;
  yy = &y;
  if (xx != 56) {
    abort();
  }
  if (**yy != 'a') {
    abort();
  }
  returnValue = 0;
  
	if (returnValue != 26) {
    abort();
  }
  exit(0);
}
