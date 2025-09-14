#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define one(x) x
#define two(x) x x
#define four(x) two(x) two(x)

int main(void) {
  char *A;

  A = "text";
  A = one("text"
          "text");
  A = two("text"
          "text");
  A = four("text"
           "text");

  return 0;
}
