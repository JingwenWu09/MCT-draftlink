#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-require-linker-plugin "" } */
/* { dg-extra-ld-options "-fuse-linker-plugin -O1" } */

void
link_error()
{
}
int
main()
{
  return 0;
}
