#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern char *strdup(const char *);

void func(char *a, char *b, char *c) {
  void *p = strdup(a);
  p = strdup(b);
  p = strdup(c);
}

int main(void) {
  func("a", "b", "c");
  return 0;
}
