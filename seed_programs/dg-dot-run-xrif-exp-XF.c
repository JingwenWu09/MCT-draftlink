#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run { xfail *-*-* } } */
/* { dg-xfail-run-if "" { empty-*-* } } */

extern void abort (void);

int
main ()
{
if(0)
    abort ();	/* A failed match doesn't override an existing XFAIL.  */
}
