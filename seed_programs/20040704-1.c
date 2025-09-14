#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort();

int main() {
  for (; 0;) {
    abort();
  label:
    return 0;
  }
  goto label;
}
