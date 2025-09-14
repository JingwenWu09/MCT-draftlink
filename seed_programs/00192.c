#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  int Count = 0;

  for (;;) {
    Count++;
    printf("%d\n", Count);
    if (Count >= 10) {
      break;
    }
  }

  return 0;
}
