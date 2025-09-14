#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */

extern void abort (void);

int a, b, c, *d, **e = &d;

int *
fn1 ()
{
  for (;;)
    {
      for (; a;)
	if (b)
	  abort ();
      break;
    }
  for (; c;)
    ;
  return &a;
}

int
main ()
{
  *e = fn1 ();

  if (!d)
    abort ();

  return 0;
}
