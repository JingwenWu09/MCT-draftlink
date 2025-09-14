#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef enum { a = 2 } T;

int main() {
  switch ((T)a) {
  case 1:
    break;
  }
  return 0;
}
