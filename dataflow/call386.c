#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
void foo () {}

int main ()
{
  int i;
  for (i = 100000; i >= 0; i--)
    {
      foo ();
      foo ();
      foo ();
      foo ();
      foo ();
      foo ();
      foo ();
      foo ();
      foo ();
      foo ();
    }
  return 0;
}
