#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a;
char b;
int main() {
  char c = 0;
  for (; c != 3; c = c + 7) {
    a = b & a;
    if (a) {
      break;
    }
  }
  return 0;
}
