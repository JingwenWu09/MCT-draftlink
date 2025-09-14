#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
foo (x, c)
{
  return x << -c;
}

main ()
{
  printf ("%x\n", foo (0xf05, -4));
}
