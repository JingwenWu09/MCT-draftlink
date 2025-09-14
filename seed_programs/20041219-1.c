#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern int printf(__const char *__restrict __format, ...);

typedef struct S {
  const char *s;
  int i;
} S;

void foo(void) {
  S dummy[2];
  unsigned i;

  for (i = 0; i < sizeof(dummy); i++) {
    ((char *)&dummy)[i] = -1;
  }
}

int bar(void) {

  S obj[2] = {{"m0"}, {"m1"}};

  if (obj[0].i == 0) {
    return 0;
  } else {
    printf("Failed: obj[0].i == '%d', expecting '0'\n", obj[0].i);
    return 1;
  }
}

int main(void) {
  foo();
  return bar();
}
