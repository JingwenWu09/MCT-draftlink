#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
int a, b, d, e;
char c;

void
foo ()
{
  for (; a; a++)
    {
      d = ((b == 0) ^ (129 + a));
      c = d * 9;
      e = c < 1;
      if (e)
	for (;;)
	  ;
    }
}

int
main ()
{
  foo ();
  return 0;
}
