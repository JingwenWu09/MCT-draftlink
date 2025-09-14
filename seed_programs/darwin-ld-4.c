#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* Test Darwin linker option -bundle.  */
/* Developed by Devang Patel <dpatel@apple.com>.  */

/* { dg-options "-bundle" } */
/* { dg-do link { target *-*-darwin* } } */

int main()
{
  return 0;
}

