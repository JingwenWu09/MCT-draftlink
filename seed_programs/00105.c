#include<stdio.h>
#include<stdlib.h>
#include<math.h>
int
main()
{
	int i;

	for(i = 0; i < 10; i++)
		if (!i)
			continue;
	
	return 0;
}
