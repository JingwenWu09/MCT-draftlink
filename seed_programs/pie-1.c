#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__thread int a;
int b;
int main() {
  return a = b;
}
