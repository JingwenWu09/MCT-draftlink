#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int __attribute__((weak)) weak() {
  return 0;
}

int main() {
  return weak();
}
