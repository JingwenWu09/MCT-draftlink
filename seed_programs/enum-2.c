#include <math.h>
#include <stdio.h>
#include <stdlib.h>

enum foo { FOO, BAR };

int main() {
  int i;
  for (i = BAR; i >= FOO; --i) {
    if (i == -1) {
      abort();
    }
  }

  exit(0);
}
