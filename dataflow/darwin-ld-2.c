#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* Test Darwin linker option -bind_at_load.  */
/* Developed by Devang Patel <dpatel@apple.com>.  */

/* { dg-options "-bind_at_load" } */
/* { dg-do link { target *-*-darwin* } } */

int main()
{
  return 0;
}

