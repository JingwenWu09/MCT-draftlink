#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#define BAR 0
#ifdef BAR
	#ifdef FOO
		XXX
		#ifdef FOO
			XXX
		#endif
	#else
		#define FOO
		#ifdef FOO
			int x = BAR;
		#endif
	#endif
#endif

int
main()
{
	return BAR;
}
