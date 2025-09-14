#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>

void __stack_chk_fail(void) {
  exit(0);
}

int main() {
  register int i;
  char foo[255];

  for (i = 0; i <= 400; i++) {
    foo[i] = 42;
  }

  return 1;
}
