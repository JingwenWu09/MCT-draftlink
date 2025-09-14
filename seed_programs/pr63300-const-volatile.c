#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
  const volatile int v = argc;
  return v - argc;
}
