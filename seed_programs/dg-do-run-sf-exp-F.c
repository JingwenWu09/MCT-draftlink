#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */
/* { dg-shouldfail "required comment" } */

int
main ()
{
    return 0;  /* We expect nonzero, so this fails.  */
}
