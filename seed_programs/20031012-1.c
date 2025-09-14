#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void foo(void) {
}

void bar(void) {
}

int main() {
  if (&foo) {
    bar();
  }
  return 0;
}
