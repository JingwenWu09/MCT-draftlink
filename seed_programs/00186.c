#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  char Buf[100];
  int Count;

  for (Count = 1; Count <= 20; Count++) {
    sprintf(Buf, "->%02d<-\n", Count);
    printf("%s", Buf);
  }

  return 0;
}
