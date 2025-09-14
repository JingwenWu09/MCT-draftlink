#include<stdio.h>
#include<stdlib.h>
#include<math.h>
void foo (int *a) {}

int main ()
{
  int a;
  if (&a == 0)
    abort ();
  else
    {
      foo (&a);
      exit (0);
    }
}
