#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef __CHAR16_TYPE__ char16_t;
typedef __CHAR32_TYPE__ char32_t;

extern void abort(void);

int main() {
  if (sizeof(char16_t) != sizeof(u'a')) {
    abort();
  }
  if (sizeof(char32_t) != sizeof(U'a')) {
    abort();
  }
}
