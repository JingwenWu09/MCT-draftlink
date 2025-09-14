#include<stdio.h>
#include<stdlib.h>
#include<math.h>
struct ge {
  char au;
  char pz[];
};

struct ge tr = { 'X', 'X', };

int
main (void)
{
  return tr.pz[0] == 'X';
}
