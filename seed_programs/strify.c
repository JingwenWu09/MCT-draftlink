#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* Test whether traditional stringify works.  */
/* { dg-do run } */


extern void abort ();
extern void exit (int);


int main ()
{
  char *c, *d;
  c="p"; 
  d="q";

  if (c[0] != 'p' || d[0] != 'q')
    abort ();

  exit (0);
}
