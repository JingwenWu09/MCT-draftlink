#include<stdio.h>
#include<stdlib.h>
#include<math.h>

int xy(int y){
	return y + 1;
}

int
main()
{
	int x;
	int y;
	
	y = 0;
	x = xy(y);
	
	if(x != 1)
		return 1;
	
	return 0;
}

