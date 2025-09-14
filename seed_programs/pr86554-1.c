#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct foo {
  __UINT32_TYPE__ x;
};

int main() {
  struct foo fi;
  fi.x = 0x800003f8;
  struct foo *f = &fi;
  int ret;

  if (f->x > 0x7FFFFFFF) {
    ret = (__INT32_TYPE__)(f->x - 0x7FFFFFFF);
  } else {
    ret = (__INT32_TYPE__)f->x - 0x7FFFFFFF;
  }

  __INT32_TYPE__ rett = ret;
  volatile __INT32_TYPE__ x = ret;
  if (rett < 1) {
    __builtin_abort();
  }
  return 0;
}
