#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main() {
  struct T {
    int x;
  };
  {
    struct T s;
    s.x = 0;
    return s.x;
  }
}
