#include<stdio.h>
#include<stdlib.h>
#include<math.h>
// { dg-do run }
// { dg-additional-options "--param asan-stack=0" }

int
main (void)
{
  char *ptr;
  {
    char my_char[9];
    ptr = &my_char[0];
  }

  *ptr = 'c';
  return 0;
}
