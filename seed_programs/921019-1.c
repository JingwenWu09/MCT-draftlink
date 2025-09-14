#include<stdio.h>
#include<stdlib.h>
#include<math.h>
void *foo[]={(void *)&("X"[0])};

main ()
{
  if (((char*)foo[0])[0] != 'X')
    abort ();
  exit (0);
}
