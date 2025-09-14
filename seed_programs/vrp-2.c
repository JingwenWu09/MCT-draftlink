#include<stdio.h>
#include<stdlib.h>
#include<math.h>
extern void abort ();
extern void exit (int);

int f (int a) {
	if (a != 2) {
		a = a > 0 ? a : -a;
		if (a == 2)
		  return 0;
		return 1;
	}
	return 1;
}

int main (int argc, char *argv[]) {
	if (f (-2))
		abort ();
	exit (0);
}

