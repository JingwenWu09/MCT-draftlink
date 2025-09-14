#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */


extern void abort (void);

int main (void)
{
  int  x0 = ( -__INT_MAX__ - 1 );
  long x1 = 0L;
  int  x2 = 0;
  int  t  = ( 0 || ( ( -__INT_MAX__ - 1 ) - (int) ( x0 - x1 ) ) );

  if ( t != 0 ) { x2 = t; abort(); }

  return 0;
}
