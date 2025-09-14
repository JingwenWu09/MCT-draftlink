#include<stdio.h>
#include<stdlib.h>
#include<math.h>
sub (int i, int array[i++])
{
  return i;
}

main()
{
  int array[10];
  exit (sub (10, array) != 11);
}
