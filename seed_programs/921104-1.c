#include<stdio.h>
#include<stdlib.h>
#include<math.h>
main ()
{
  unsigned long val = 1;

  if (val > (unsigned long) ~0)
    abort();
  exit (0);
}
