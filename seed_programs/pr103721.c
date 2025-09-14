#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int ipos = 0;
int f(int world) {
  int searchVolume = world;
  int currentVolume = 0;
  while (currentVolume != searchVolume && searchVolume) {
    currentVolume = searchVolume;
    if (ipos != 0) {
      searchVolume = 0;
    } else {
      searchVolume = 1;
    }
  }
  return (currentVolume);
}
int main() {
  const int i = f(1111);
  __builtin_printf("%d\n", (int)(i));
  if (i != 1) {
    __builtin_abort();
  }
  return 0;
}
