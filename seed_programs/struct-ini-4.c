#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct s {
  int a[3];
  int c[3];
};

struct s s = {.c = {1, 2, 3}};

main() {
  if (s.c[0] != 1) {
    abort();
  }
  exit(0);
}
