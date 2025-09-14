#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int globvar;

void leave(int i) {
  if (i != 0) {
    abort();
  }
  exit(0);
}

void doit() {
  srand(12);
  globvar = rand();
  if (rand() > 0) {
    globvar = 0;
  }
  leave(globvar);
}

int main() {
  doit();
}
