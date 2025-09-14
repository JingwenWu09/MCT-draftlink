#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>
typedef int word __attribute__((mode(word)));

int main() {
  if ((sizeof(word) > 1) && ((__UINTPTR_TYPE__)malloc(1) & (sizeof(word) - 1))) {
    abort();
  }
  return 0;
}
