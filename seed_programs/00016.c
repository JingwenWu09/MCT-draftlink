#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main() {
  int arr[2];
  int *p;

  p = &arr[1];
  *p = 0;
  return arr[1];
}
