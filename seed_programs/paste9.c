#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <string.h>

extern void abort(void);

#define SS(str, args...) "  " str "\n", ##args

int main() {
  const char *s = SS("foo");

  if (strchr(s, '\n') == NULL) {
    abort();
  }

  return 0;
}
