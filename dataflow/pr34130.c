#include<stdio.h>
#include<stdlib.h>
#include<math.h>
extern void abort (void);
int foo (int i)
{
  return -2 * __builtin_abs(i - 2);
}
int main()
{
  if (foo(1) != -2
      || foo(3) != -2)
    abort ();
  return 0;
}
