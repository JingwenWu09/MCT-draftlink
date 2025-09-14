#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-options "-std=c99" } */

long long a = -1;
long long llabs (long long);
void abort (void);
int
main()
{
  if (llabs (a) != 1)
    abort ();
  return 0;
}
long long llabs (long long b)
{
    return -b;
}
