#include<stdio.h>
#include<stdlib.h>
#include<math.h>
int
main()
{
	struct { int x; } s = { 0 };
	return s.x;
}
