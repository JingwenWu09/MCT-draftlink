#include <math.h>
#include <stdio.h>
#include <stdlib.h>

char buffer[32] = "foo bar";

int main() {
  __builtin___clear_cache(buffer, buffer + 32);
  return 0;
}
