#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
int main() {
	int *i;
  int k = 2;
  i = &k;
	
	int local_p1 = 1;
	int x1 = 0;
  int *j1;
  int **p1;
  if (local_p1) {
    p1 = &j1;
  } else {
    p1 = &i;
  }
  *p1 = &x1;
  *i = 1;
	int returnValue1 = x1;

  if (returnValue1 != 0 || k != 1) {
    abort();
  }

	int local_p2 = 0;
	int x2 = 0;
  int *j2;
  int **p2;
  if (local_p2) {
    p2 = &j2;
  } else {
    p2 = &i;
  }
  *p2 = &x2;
  *i = 1;
	int returnValue2 = x2;

  if (returnValue2 != 1) {
    abort();
  }
  return 0;
}
