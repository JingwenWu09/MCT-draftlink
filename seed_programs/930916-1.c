#include<stdio.h>
#include<stdlib.h>
#include<math.h>
f (n)
     unsigned n;
{
  if ((int) n >= 0)
    abort ();
}

main ()
{
  unsigned x = ~0;
  f (x);
  exit (0);
}
