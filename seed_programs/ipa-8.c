#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdio.h>
static int g(int b, int c) {
  printf("%d %d\n", b, c);
}
static int f(int a) {

  if (a > 0) {
    g(a, 3);
  } else {
    g(a, 5);
  }
}
int main() {
  f(7);
  return 0;
}
