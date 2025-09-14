#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#if defined(FOO)
int a;
#elif !defined(FOO) && defined(BAR)
int b;
#elif !defined(FOO) && !defined(BAR)
int c;
#else
int d;
#endif

int
main(void)
{
	return c;
}
