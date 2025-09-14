#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run { xfail *-*-* } } */
/* { dg-shouldfail "required comment" } */

extern void abort (void);

int
main ()
{
if(0)
    abort ();  /* We want nonzero, but expect to fail; XPASS.  */
}
