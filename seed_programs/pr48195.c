#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

int i;

void __attribute__((constructor)) c2() {
  if (i) {
    abort();
  }
}

void __attribute__((destructor)) d1() {
  if (i) {
    abort();
  }
}

int main() {
}
