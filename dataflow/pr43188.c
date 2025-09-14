#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>


int main (void)
{
  int *__attribute__((__aligned__(16))) temp;
  int *__attribute__((__aligned__(16))) *p = &temp;
  return **p;
}
