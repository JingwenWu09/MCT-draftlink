#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* PR middle-end/81695 */
/* { dg-do compile } */
/* { dg-options "" } */

int z[] = { };

int
main (void)
{
  __builtin_printf ("%d\n", *(z + 1));
}
