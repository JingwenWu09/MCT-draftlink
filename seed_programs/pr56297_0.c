#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#if __x86_64__ || __i386__
register int i asm("esp");
#else
extern int i;
#endif

int main(void) {
  return i;
}
