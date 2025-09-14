#include<stdio.h>
#include<stdlib.h>
#include<math.h>
extern void abort(void);
extern void exit(int);

unsigned int foo(unsigned int a)
{
  return ((unsigned char)(a + 1)) * 4;
}

int main(void)
{
  if (foo((unsigned char)~0))
    abort ();
  exit(0);
}
