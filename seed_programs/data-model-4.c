#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdio.h>
int main() {
  FILE *f = fopen("conftest.out", "w");
  return ferror(f) || fclose(f) != 0;

  ;
  return 0;
}
