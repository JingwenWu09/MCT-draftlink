#include <math.h>
#include <stdio.h>
#include <stdlib.h>

long level = 0;
extern long foo(void);
extern long bar(void);

#ifdef STACK_SIZE
#define DEPTH ((STACK_SIZE) / 512 + 1)
#else
#define DEPTH 500
#endif

int main(void) {
  if (foo() == -42) {
    exit(0);
  }

  abort();
}

long foo(void) {
  long tmp = ++level;
  return bar() + tmp;
}

long bar(void) {
  long tmp = level;
  return tmp > DEPTH - 1 ? -42 - tmp : foo() - tmp;
}
