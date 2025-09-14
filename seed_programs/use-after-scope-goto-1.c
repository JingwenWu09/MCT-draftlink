#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
  int a = 123;
  int b = 123;
  int c = 123;
  int d = 123;
  int e = 123;
  int f = 123;

  int *ptr;
  int *ptr2;
  int *ptr3;
  int *ptr4;
  int *ptr5;
  int *ptr6;
  if (argc == 0) {
    ptr = &a;
    *ptr = 1;
    ptr2 = &b;
    *ptr2 = 1;
    ptr3 = &c;
    *ptr3 = 1;
    ptr4 = &d;
    *ptr4 = 1;
    ptr5 = &e;
    *ptr5 = 1;
    ptr6 = &f;
    *ptr6 = 1;
    return 0;
  } else {
    ptr = &a;
    *ptr = 1;
    ptr2 = &b;
    *ptr2 = 1;
    ptr3 = &c;
    *ptr3 = 1;
    ptr4 = &d;
    *ptr4 = 1;
    ptr5 = &e;
    *ptr5 = 1;
    ptr6 = &f;
    *ptr6 = 1;
    return 0;
  }

  return 0;
}
