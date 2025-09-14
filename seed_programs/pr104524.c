#include<stdio.h>
#include<stdlib.h>
#include<math.h>
int src[1];

int
main (int c, char **a)
{
  __builtin_memcpy (*a, src, c);

  return 0;
}
