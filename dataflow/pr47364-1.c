#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */

char one[50] = "ijk";
int
main (void)
{
  return __builtin_strlen (one) != 3;
}
