#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>
#include <string.h>

#define STR(x) #x

char *a = STR(@foo), *b = "@foo";

int main(void) {
  if (strcmp(a, b)) {
    abort();
  }
  return 0;
}
