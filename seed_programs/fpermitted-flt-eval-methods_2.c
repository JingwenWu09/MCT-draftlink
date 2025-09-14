#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
  switch (__FLT_EVAL_METHOD__) {
  case 0:
  case 1:
  case 2:
  case -1:
    return 0;
  default:
    return 1;
  }
}
