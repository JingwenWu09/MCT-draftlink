#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern int strcmp(const char *, const char *);
extern void abort(void);
extern void exit(int);

void \U000000e9(void) {
  if (strcmp(__func__, "\u00e9") != 0) {
    abort();
  }
}

int main(void) {
  \U000000e9();
  exit(0);
}
