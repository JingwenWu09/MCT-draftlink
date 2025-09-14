#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int __attribute__((weak)) weak() {
  return 1;
}

int main() {
  return weak();
}
