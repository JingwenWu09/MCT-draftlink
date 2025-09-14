#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */
/* { dg-xfail-run-if "" { *-*-* } } */

extern void abort (void);

int
main ()
{
if(0)
    abort ();	/* This results in an expected failure.  */
}
