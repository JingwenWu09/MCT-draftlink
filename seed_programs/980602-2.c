#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#if __INT_MAX__ < 2147483647
int main(void) {
  exit(0);
}
#else
struct {
  unsigned bit : 30;
} t;

int main() {
  if (!(t.bit++)) {
    exit(0);
  } else {
    abort();
  }
}
#endif
