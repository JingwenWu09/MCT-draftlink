#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void abort(void);

static int \U000000c0 = 1;
static int \U000000c1 = 2;
static int \U000000c2 = 3;
static int wh\U000000ff = 4;
static int a\U000000c4b\U00000441\U000003b4e = 5;

int main(void) {

  if (\U000000c0 != 1) {
    abort();
  }
  if (\U000000c1 != 2) {
    abort();
  }
  if (\U000000c2 != 3) {
    abort();
  }
  if (wh\U000000ff != 4) {
    abort();
  }
  if (a\U000000c4b\U00000441\U000003b4e != 5) {
    abort();
  }

  return 0;
}
