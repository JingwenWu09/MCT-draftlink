#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#define M(x) x
#define A(aa,bb) aa(bb)

int
main(void)
{
	char *a = A(M,"hi");

	return (a[1] == 'i') ? 0 : 1;
}
