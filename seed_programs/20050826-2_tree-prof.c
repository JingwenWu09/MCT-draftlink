#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct rtattr {
  unsigned short rta_len;
  unsigned short rta_type;
};

extern void abort(void);

int test(void) {
  struct rtattr rt[2];
  struct rtattr *rta[14];
  int i;

  rt[0].rta_len = sizeof(struct rtattr) + 8;
  rt[0].rta_type = 0;
  rt[1] = rt[0];
  for (i = 0; i < 14; i++) {
    rta[i] = &rt[0];
  }

  struct rtattr **rtap = rta;
  int returnValue1;
	int flag1 = 1;
  for (i = 1; i <= 14; i++) {
    struct rtattr *attr = rtap[i - 1];
    if (attr) {
      if (attr->rta_len - sizeof(struct rtattr) < 4) {
        returnValue1 = -22;
        flag1 = 0;
				break ;
      }
      if (i != 9 && i != 8) {
        rtap[i - 1] = attr + 1;
      }
    }
  }
  if(flag1) {
		returnValue1 = 0;
	}
  if (returnValue1 != 0) {
    abort();
  }
  for (i = 0; i < 14; i++) {
    if (rta[i] != &rt[i != 7 && i != 8]) {
      abort();
    }
  }
  for (i = 0; i < 14; i++) {
    rta[i] = &rt[0];
  }
  rta[1] = 0;
  rt[1].rta_len -= 8;
  rta[5] = &rt[1];

  rtap = rta;
  int returnValue2;
	int flag2 = 1;
  for (i = 1; i <= 14; i++) {
    struct rtattr *attr = rtap[i - 1];
    if (attr) {
      if (attr->rta_len - sizeof(struct rtattr) < 4) {
        returnValue2 = -22;
        flag2 = 0;
				break ;
      }
      if (i != 9 && i != 8) {
        rtap[i - 1] = attr + 1;
      }
    }
  }
  if(flag2) {
		returnValue2 = 0;
	}
  if (returnValue2 != -22) {
    abort();
  }
  for (i = 0; i < 14; i++) {
    if (i == 1 && rta[i] != 0) {
      abort();
    } else if (i != 1 && i <= 5 && rta[i] != &rt[1]) {
      abort();
    } else if (i > 5 && rta[i] != &rt[0]) {
      abort();
    }
  }
  return 0;
}

int main(void) {
  int i;
  for (i = 0; i < 100; i++) {
    test();
  }
  return 0;
}
