#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-require-visibility "" } */

extern void abort (void);

int __attribute__((weak,visibility("hidden"))) foo (void)
{
  return 1;
}

int main()
{
  if (foo() != 1)
    abort ();
  return 0;
}
