#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);

union U {
  int i, j[4];
};

int main() {
  union U t = {};
  int i;

  for (i = 0; i < 4; ++i) {
    if (t.j[i] != 0) {
      abort();
    }
  }

  return 0;
}
