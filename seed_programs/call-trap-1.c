#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void exit(int);
extern void abort(void);

int foo(void) {
  exit(0);
  return 0;
}

void bar(void) {
}

int main(void) {
  ((long (*)(int))bar)(foo());
  abort();
}
