#include<stdio.h>
#include<stdlib.h>
#include<math.h>
int
main()
{
	int x;
	int *p;
	
	x = 4;
	p = &x;
	*p = 0;

	return *p;
}
