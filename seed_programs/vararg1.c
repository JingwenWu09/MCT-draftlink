#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define count(y...) count1(, ##y)
#define count1(y...) count2(y, 1, 0)
#define count2(_, x0, n, y...) n
#if count() != 0 || count(A) != 1
#error Incorrect vararg argument counts
#endif

#include <string.h>

#define SS(str, args...) "  " str "\n", ##args

int main() {
  const char *s = SS("foo");
  return strchr(s, '\n') == NULL;
}
