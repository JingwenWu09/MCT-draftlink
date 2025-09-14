#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

typedef unsigned char uint8_t;

struct MyType {
  uint8_t flag1 : 2;
  uint8_t flag2 : 1;
  uint8_t flag3 : 1;

  uint8_t flag4;

} __attribute__((packed));

int main(void) {
  struct MyType a;
  struct MyType *b = &a;

  b->flag1 = 0;
  b->flag2 = 0;
  b->flag3 = 0;

  b->flag4 = 0;

  b->flag4++;

  if (b->flag1 != 0) {
    abort();
  }

  return 0;
}
