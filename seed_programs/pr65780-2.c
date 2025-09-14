#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* PR target/65780 */
/* { dg-do link { target *-*-linux* *-*-gnu* *-*-uclinux* } } */
/* { dg-require-effective-target pie } */
/* { dg-options "-O2 -fpie" } */

int optopt;

int
main ()
{
  optopt = 4;
  return 0;
}
