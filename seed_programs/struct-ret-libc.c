#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>

int main() {
  div_t d = div(20, 5);
  if ((d.quot != 4) || (d.rem)) {
    abort();
  }
  exit(0);
}
