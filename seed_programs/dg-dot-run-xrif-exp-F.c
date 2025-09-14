#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run { xfail *-empty-* } } */
/* { dg-xfail-run-if "" { empty-*-* } } */

extern void abort (void);

int
main ()
{
if(0)
    abort ();	/* Neither xfail list matched, so fail.  */
}
