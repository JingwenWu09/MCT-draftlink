#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  int temp;
  int *g = &temp;
  switch (*g) {
  case 0: {
    switch (*g) {
    case 0:
      *g = 1;
      break;
    case 1:
    case 2:
      *g = 1;
      break;
    case 3:
    case 4:
      *g = 1;
      break;
    }
    break;
  }
  case 1: {
    switch (*g) {
    case 0:
      *g = 1;
      break;
    case 1:
    case 2:
      *g = 1;
      break;
    case 3:
    case 4:
      *g = 1;
      break;
    }
  }
  }
  return 0;
}
