#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* Test insane -O argument.  */
/* { dg-do compile } */
/* { dg-options "-O16384" } */

__attribute__((__optimize__(16396), __noinline__)) void
foo (void)
{
}

int
main (void)
{
  foo ();
  return 0;
}
