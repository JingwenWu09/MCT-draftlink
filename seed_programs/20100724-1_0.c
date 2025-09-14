#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void baz(void) {
  __builtin_abort();
}
void foo(void) {
  baz();
}
int main() {
  return 0;
}
