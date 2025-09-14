#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */
/* { dg-shouldfail "comment" { *-*-* } { "*" } { "" } } */

extern void abort (void);

int
main ()
{
if(0)
    abort ();  /* We expect nonzero exit, so this passes.  */
}
