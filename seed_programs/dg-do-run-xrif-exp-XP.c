#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */
/* { dg-xfail-run-if "" { *-*-* } } */

extern void abort (void);

int
main ()
{
    return 0;	/* This results in unexpected pass.  */
}
