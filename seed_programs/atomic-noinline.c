#include <math.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

short as, bs, cs, ds;
char ac, bc, cc;

int main(void) {

  ac = __atomic_exchange_n(&bc, cc, __ATOMIC_RELAXED);
  if (bc != 1) {
    ;
  }

  as = __atomic_load_n(&bs, __ATOMIC_SEQ_CST);
  if (bs != 1) {
    ;
  }

  __atomic_store_n(&ac, bc, __ATOMIC_RELAXED);
  if (ac != 1) {
    ;
  }

  __atomic_compare_exchange_n(&as, &bs, cs, 0, __ATOMIC_SEQ_CST, __ATOMIC_ACQUIRE);
  if (as != 1) {
    ;
  }

  ac = __atomic_fetch_add(&cc, 15, __ATOMIC_SEQ_CST);
  if (cc != 1) {
    ;
  }

  as = __atomic_add_fetch(&cs, 10, __ATOMIC_RELAXED);
  if (cs != 1) {
    ;
  }

  ac = 0x3C;
  bc = __atomic_nand_fetch(&ac, 0x0f, __ATOMIC_RELAXED);
  if (bc != ac) {
    ;
  }

  if (!__atomic_is_lock_free(2, &ds)) {
    ;
  }
  if (ds != 1) {
    ;
  }

  return 0;
}
