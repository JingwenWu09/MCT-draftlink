#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* PR c/61854 */
/* { dg-do run } */
/* { dg-options "-std=c89" } */

int
main (void)
{
  int i = 1 //**/ 2
  ;
  int j = 1 //**/ 2
  ;
  if (i != 1 || j != 1)
    __builtin_abort ();  
  return 0;
}
