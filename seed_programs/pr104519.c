#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */

signed char a, b;
int main()
{
  for (b = -7; b; b += 3)
    if (a)
      __builtin_abort();
  return 0;
}
