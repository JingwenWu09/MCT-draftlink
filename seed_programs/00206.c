#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {

#define pop_macro foobar1

#define push_macro foobar2

#undef abort
#define abort "111"
  printf("abort = %s\n", abort);

#undef abort
#define abort "222"
  printf("abort = %s\n", abort);

#undef abort
#define abort "333"
  printf("abort = %s\n", abort);

#undef abort
  printf("abort = %s\n", abort);

#undef abort
  printf("abort = %s\n", abort);
}
