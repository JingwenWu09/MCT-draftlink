#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void link_error();
struct a {};
int main() {
  struct a b[2];
  if (&b[0] != &b[1]) {
    link_error();
  }
  return 0;
}
