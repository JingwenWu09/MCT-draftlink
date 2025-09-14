#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  __builtin_printf("%s %s\n", __DATE__, __TIME__);

  __builtin_printf("%s %s\n", __DATE__, __TIME__);
  return 0;
}
