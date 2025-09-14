#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */
/* { dg-shouldfail "comment" { unknown-*-* } { "*" } { "" } } */

extern void abort (void);

int
main ()
{
if(0)
    abort ();  /* Directive is ignored so we expect zero; this fails.  */
}
