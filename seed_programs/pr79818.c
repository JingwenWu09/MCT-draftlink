#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static void __attribute__((noinline, noclone)) func(void) {
  signed char c = 0;

  for (; c >= 0; c++) {
    ;
  }

  if (c != -128) {
    __builtin_abort();
  }
}

int main(int argc, char *argv[]) {
  func();
  return 0;
}
