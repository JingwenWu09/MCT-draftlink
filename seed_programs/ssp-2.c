#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>

void __stack_chk_fail(void) {
  exit(0);
}

void overflow() {
  register int i = 0;
  char foo[30];

  for (i = 0; i < 50; i++) {
    foo[i] = 42;
  }
}

int main(void) {
  overflow();
  return 1;
}
