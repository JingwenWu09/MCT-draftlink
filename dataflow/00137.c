#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#define x(y) #y

int
main(void)
{
	char *p;
	p = x(hello)  " is better than bye";

	return (*p == 'h') ? 0 : 1;
}
