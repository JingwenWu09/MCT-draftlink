#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main() {
  __WCHAR_TYPE__ c = -1;

#if L'\x0' - 1 < 0
  if (c > 0) {
    abort();
  }
#else
  if (c < 0)
    abort();
#endif

  return 0;
}
