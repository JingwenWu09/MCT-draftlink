#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  int Count;

  int Array[10] = {12, 34, 56, 78, 90, 123, 456, 789, 8642, 9753};

  for (Count = 0; Count < 10; Count++) {
    printf("%d: %d\n", Count, Array[Count]);
  }

  int Array2[10] = {
      12, 34, 56, 78, 90, 123, 456, 789, 8642, 9753,
  };

  for (Count = 0; Count < 10; Count++) {
    printf("%d: %d\n", Count, Array2[Count]);
  }

  return 0;
}
