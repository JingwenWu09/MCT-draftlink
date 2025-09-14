#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void link_error(void);
int main() {
  if (&"<12ers"[1] == 0) {
    link_error();
  }
  return 0;
}
