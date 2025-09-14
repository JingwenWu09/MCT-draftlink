#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
f(short *p)
{
  short x = *p;
  return (--x < 0);
}

main()
{
  short x = -10;
  if (!f(&x))
    abort();
  exit(0);
}
