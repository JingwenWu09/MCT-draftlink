#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void *buf[5];

void fjmp(void) {
  __builtin_longjmp(buf, 1);
}

int main(void) {
  int last = 0;

  if (__builtin_setjmp(buf) == 0) {
    __builtin_printf("True  branch\n");
    while (1) {
      last = 1;
      fjmp();
    }
  } else {
    __builtin_printf("False branch\n");
  }

  return 0;
}
