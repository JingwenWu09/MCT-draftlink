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
      int returnValue1;
			int flag1 = 1;
      if (ap->exp > bp->exp) {
        returnValue1 = 1;
        flag1 = 0;
      } else if (ap->exp < bp->exp) {
        returnValue1 = -1;
        flag1 = 0;
      } else if (ap->sig > bp->sig) {
        returnValue1 = 1;
        flag1 = 0;
      }
      if(flag1) {
				returnValue1 = -(ap->sig < bp->sig);
			}
      if (i < j && returnValue1 != -1) {
        abort();
      }

      ap = &a[i];
      bp = &a[j];
      int returnValue2;
			int flag2 = 1;
      if (ap->exp > bp->exp) {
        returnValue2 = 1;
        flag2 = 0;
      } else if (ap->exp < bp->exp) {
        returnValue2 = -1;
        flag2 = 0;
      } else if (ap->sig > bp->sig) {
        returnValue2 = 1;
        flag2 = 0;
      }
      if(flag2) {
				returnValue2 = -(ap->sig < bp->sig);
			}
      if (i == j && returnValue2 != 0) {
        abort();
      }

      ap = &a[i];
      bp = &a[j];
      int returnValue3;
			int flag3 = 1;
      if (ap->exp > bp->exp) {
        returnValue3 = 1;
        flag3 = 0;
      } else if (ap->exp < bp->exp) {
        returnValue3 = -1;
        flag3 = 0;
      } else if (ap->sig > bp->sig) {
        returnValue3 = 1;
        flag3 = 0;
      }
      if(flag3) {
				returnValue3 = -(ap->sig < bp->sig);
			}
      if (i > j && returnValue3 != 1) {
        abort();
      }
    }
  }
  return 0;
}
