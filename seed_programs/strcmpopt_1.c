#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>
#include <string.h>

int cmp1(char *p) {
  return strncmp(p, "fis", 4);
}
int cmp2(char *q) {
  return strncmp("fis", q, 4);
}

int main() {

  char *p = "fish";
  char *q = "fis\0";

  if (cmp1(p) == 0 || cmp2(q) != 0) {
    abort();
  }

  return 0;
}
