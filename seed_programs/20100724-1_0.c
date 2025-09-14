#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-lto-do link } */

void baz(void)
{
  __builtin_abort ();
}
void foo(void)
{
  baz();
}
int main() { return 0; }
