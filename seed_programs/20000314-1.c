#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct S {
  char a[5];
};

int main() {
  struct S *p;
  int i = -1;

  if (p->a[-i]) {
    return 1;
  }
}
