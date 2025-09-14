#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  char *ptr;
  char *ptr2;
  char my_char[9];
  ptr = &my_char[0];

  *(ptr2 + 9) = 'c';
}
