#include <math.h>
#include <stdio.h>
#include <stdlib.h>

enum E {
  A,
  B,
  C,
};

int __attribute__((noipa)) foo(enum E e) {
  switch (e) {
  case A:
    return 0;
  case B:
    return 1;
  case C:
    return 2;
  }

  return -1;
}

int main() {
  if (foo(A) != 0) {
    __builtin_abort();
  }

  if (foo(B) != 1) {
    __builtin_abort();
  }

  if (foo(C) != 2) {
    __builtin_abort();
  }

  return 0;
}
