#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
extern void abort (void);

int f(unsigned int x, int n)
{
    return ((int)x) / (1 << n);
}

int main()
{
  if (f(-1, 1) != 0)
    abort ();
  return 0;
}
