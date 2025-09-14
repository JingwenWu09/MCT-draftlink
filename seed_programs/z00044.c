#include<stdio.h>
#include<stdlib.h>
#include<math.h>
struct T;

struct T {
	int x;
};

int
main()
{
	struct T v;
	{ struct T { int z; }; }
	v.x = 2;
	if(v.x != 2)
		return 1;
	return 0;
}
