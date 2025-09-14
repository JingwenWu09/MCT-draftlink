#include <math.h>
#include <stdio.h>
#include <stdlib.h>
__extension__ typedef __PTRDIFF_TYPE__ ptr_t;
ptr_t *wm_TR;
ptr_t *wm_HB;
ptr_t *wm_SPB;

ptr_t mem[100];

main() {
  mem[99] = (ptr_t)mem;

  ptr_t *mr_TR = mem + 100;
  ptr_t *mr_SPB = mem + 6;
  ptr_t *mr_HB = mem + 8;
  ptr_t *reg1 = mem + 99;
  ptr_t *reg2 = mem + 99;

  ptr_t *x = mr_TR;

  for (;;) {
    if (reg1 < reg2) {
      if (x != mr_TR) {
    		abort();
  		}
			exit(0);
    }
    if ((ptr_t *)*reg1 < mr_HB && (ptr_t *)*reg1 >= mr_SPB) {
      *--mr_TR = *reg1;
    }
    reg1--;
  }

  if (x != mr_TR) {
    abort();
  }
  exit(0);
}
