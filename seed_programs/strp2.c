#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>
#include <string.h>

#define hash_hash # ## #
#define mkstr(a) #a
#define in_between(a) mkstr(a)
#define join(c, d) in_between(c hash_hash d)

const char p[] = join(x, y);
const char q[] = "x ## y";

int main(void) {
  if (strcmp(p, q)) {
    abort();
  }
  return 0;
}
