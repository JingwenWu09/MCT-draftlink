#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */
/* { dg-xfail-run-if "" { empty-*-* } } */

extern void abort (void);

int
main ()
{
    return 0;	/* This results in a pass.  */
}
