#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */

char a;
int b;

int main ()
{
  unsigned char c = a, d = a;
  b = d == 0 ? c : c % d;
  return 0;
}
