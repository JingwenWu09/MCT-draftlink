#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
long long
foo (a)
     int a;
{
  return a;
}

main ()
{
  printf ("%d\n", (int) (foo (-1) >> 32));
}
