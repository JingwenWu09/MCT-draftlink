#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct sreal {
  unsigned sig;
  int exp;
};

struct sreal a[] = {{0, 0}, {1, 0}, {0, 1}, {1, 1}};

int main() {
  int i, j;
  for (i = 0; i <= 3; i++) {
    for (j = 0; j < 3; j++) {

      struct sreal *ap = &a[i];
      struct sreal *bp = &a[j];
      int returnValue;
			int flag = 1;
      if (ap->exp > bp->exp) {
        returnValue = 1;
        flag = 0;
      } else if (ap->exp < bp->exp) {
        returnValue = -1;
        flag = 0;
      } else if (ap->sig > bp->sig) {
        returnValue = 1;
        flag = 0;
      } else if (ap->sig < bp->sig) {
        returnValue = -1;
        flag = 0;
      }
      if(flag) {
				returnValue = 0;
			}
      if (i < j && returnValue != -1) {
        abort();
      }

      ap = &a[i];
      bp = &a[j];
			flag = 1;
      if (ap->exp > bp->exp) {
        returnValue = 1;
        flag = 0;
      } else if (ap->exp < bp->exp) {
        returnValue = -1;
        flag = 0;
      }else if (ap->sig > bp->sig) {
        returnValue = 1;
        flag = 0;
      }else if (ap->sig < bp->sig) {
        returnValue = -1;
        flag = 0;
      }
      if(flag) {
				returnValue = 0;
			}
      if (i == j && returnValue != 0) {
        abort();
      }

      ap = &a[i];
      bp = &a[j];
			flag = 1;
      if (ap->exp > bp->exp) {
        returnValue = 1;
        flag = 0;
      } else if (ap->exp < bp->exp) {
        returnValue = -1;
        flag = 0;
      } else if (ap->sig > bp->sig) {
        returnValue = 1;
        flag = 0;
      } else if (ap->sig < bp->sig) {
        returnValue = -1;
        flag = 0;
      }
      if(flag) {
				returnValue = 0;
			}
      if (i > j && returnValue != 1) {
        abort();
      }
    }
  }
  return 0;
}
