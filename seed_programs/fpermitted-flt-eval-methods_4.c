#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define __STDC_WANT_IEC_60559_TYPES_EXT__

#include <float.h>

int main(int argc, char **argv) {
  switch (__FLT_EVAL_METHOD__) {
  case 0:
  case 1:
  case 2:
  case 16:
  case -1:
    return 0;
  default:
    return 1;
  }
}
