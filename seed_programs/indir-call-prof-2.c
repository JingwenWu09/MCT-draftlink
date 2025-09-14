#include <math.h>
#include <stdio.h>
#include <stdlib.h>

volatile int one;
static int add1(int val) {
  return val += one;
}

static int sub1(int val) {
  return val -= one;
}

static int do_op(int val, int (*fnptr)(int)) {
  return fnptr(val);
}

int main(void) {
  int i, val = 0;
  for (i = 0; i < 10000000; i++) {
    val = do_op(val, add1);
    val = do_op(val, sub1);
  }
  return val;
}
