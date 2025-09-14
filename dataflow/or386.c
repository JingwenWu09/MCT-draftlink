#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
typedef int xtype;

xtype
foo (a)
     xtype a;
{
  return a | 0x7f;
}

main ()
{
  printf ("%08x\n", foo (-1));
}
