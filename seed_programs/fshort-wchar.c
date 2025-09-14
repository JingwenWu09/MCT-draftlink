#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);

int main() {
  __WCHAR_TYPE__ w = ~(__WCHAR_TYPE__)0;

  if (w == __WCHAR_MAX__) {
    abort();
  }

  return 0;
}
