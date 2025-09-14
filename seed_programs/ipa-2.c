#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdio.h>
int g(int b, int c) {
  printf("%d %d\n", b, c);
}
int f(int a) {

  if (a++ > 0) {
    g(a, 3);
  }
}
int main() {
  int i;
  for (i = 0; i < 100; i++) {
    f(7);
  }
  return 0;
}
