#include<stdio.h>
#include<stdlib.h>
#include<math.h>
extern void abort(void);
typedef void (*frob)();
frob f[] = {abort};

int main(void)
{
  exit(0);
}
