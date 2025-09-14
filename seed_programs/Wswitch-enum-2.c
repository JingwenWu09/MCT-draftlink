#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef enum { a = 2 } T;

int main() {
  T x = a;
  switch (x) {
  case a ... 3:
    break;
  }
  switch (x) {
  case 1 ... a:
    break;
  }
  return 0;
}
