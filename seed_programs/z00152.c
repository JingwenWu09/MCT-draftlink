#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#undef  line
#define line 1000

#line line
#if 1000 != __LINE__
	#error "  # line line" not work as expected
#endif

int
main()
{
	return 0;
}
