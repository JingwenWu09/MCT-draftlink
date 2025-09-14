#include<stdio.h>
#include<stdlib.h>
#include<math.h>
int
main()
{
	struct S { struct S *p; int x; } s;
	
	s.x = 0;
	s.p = &s;
	return s.p->p->p->p->p->x;
}

