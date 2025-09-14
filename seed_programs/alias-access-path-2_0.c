#include <math.h>
#include <stdio.h>
#include <stdlib.h>
typedef int (*fnptr)();

__attribute__((used)) struct a { void *array[2]; } a, *aptr = &a;

__attribute__((used)) struct b { struct a a; } * bptr;

static void inline_me_late(int argc) {
  if (argc == -1) {
    bptr->a.array[1] = bptr;
  }
}

int main(int argc) {
  aptr->array[0] = 0;
  inline_me_late(argc);
  if (__builtin_constant_p(aptr->array[0] == 0)) {
    __builtin_abort();
  }
  return 0;
}
