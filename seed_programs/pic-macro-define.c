#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#if defined __PIC__
int main() {
  return 0;
}
#else
error "NO __PIC__ DEFINED"
#endif
