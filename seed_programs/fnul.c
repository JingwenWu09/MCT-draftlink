#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
main ()
{
  int i;
  int f;

  for (i = 0;; i--)
    {
      f = 0;

      if ((i & (i - 1)) == 0)
	{
	  printf ("d");
	  f = 1;
	}
      if ((i & -i) == i)
	{
	  printf ("t");
	  f = 1;
	}
      if (f)
	printf ("%d\n", i);
    }
}
