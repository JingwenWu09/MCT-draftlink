#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main() {
  char a[10];
  strcpy(a, "abcdef");
  printf("%s\n", &a[1]);

  return 0;
}
