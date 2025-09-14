#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void __attribute__((weak)) func(void) {
}

int main() {
  func();
  return 0;
}
