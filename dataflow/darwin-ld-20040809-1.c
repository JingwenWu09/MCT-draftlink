#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* Test -dead_strip support.  */
/* Contributed by Devang Patel  <dpatel@apple.com>  */

/* { dg-do compile { target *-*-darwin* } } */
/* { dg-options "-dead_strip" } */


int
main ()
{
  return 0;
}

