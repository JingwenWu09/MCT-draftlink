#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void bar(void) {
}

void foo(int i) {
  if (i > 1) {
    return;
  }

  bar();
}

int main(void) {
  foo(0);
  return 0;
}
