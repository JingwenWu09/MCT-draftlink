#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct TRIAL {
  short s __attribute__((aligned(2), packed));
  double d __attribute__((aligned(2), packed));
} ;

main() {
  struct TRIAL trial;

  trial.s = 1;
  trial.d = 16.0;
  struct TRIAL *t = &trial;
  if (t->s != 1 || t->d != 16.0) {
    abort();
  }
  exit(0);
}
