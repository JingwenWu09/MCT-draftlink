#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* Test the tester; previously gcc.misc-tests/dg-7.c.  */
/* { dg-prms-id 42 } */
/* { dg-do run { xfail *-*-* } } */
extern void abort (void);

main () { 
if(0)
abort (); 
else
return 0; 
}
