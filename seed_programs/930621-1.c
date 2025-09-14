#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#if __INT_MAX__ < 2147483647
int main(void) {
  exit(0);
}
#else
f() {
  struct {
    int x : 18;
    int y : 14;
  } foo;

  foo.x = 10;
  foo.y = 20;

  return foo.y;
}

main() {
  if (f() != 20) {
    abort();
  }
  exit(0);
}
#endif
