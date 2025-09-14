#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static int __attribute__((noipa)) foo(char x) {
  switch (x) {
  case '"':
  case '<':
  case '>':
  case '\\':
  case '^':
  case '`':
  case '{':
  case '|':
  case '}':
    return 0;
  }
  return 1;
}

int main() {
  if (foo('h') == 0) {
    __builtin_abort();
  }
  return 0;
}
