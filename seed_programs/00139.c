#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/*
 * f(2) will expand to 2*g, which will expand to 2*f, and in this
 * moment f will not be expanded because the macro definition is
 * a function alike macro, and in this case there is no arguments.
 */

int ff(int a, int b){
	return a*b;
}

int
main(void)
{
        int f = 0;

        return ff(2, f);
}
