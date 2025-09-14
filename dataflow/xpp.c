#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
foo (a)
{
  a++;
  if (a < 10)
    return 1;
  return a;
}

main ()
{
  printf ("%d\n", foo ((1 << 31) - 1));
}
