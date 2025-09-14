#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */

extern void abort(void);

int try (int *a)
{
  return a + -1 > a;
}

int main(void)
{
  int bla[100];

  if (try (bla + 50))
    abort ();

  return 0;
}
