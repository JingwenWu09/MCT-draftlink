#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */
/* { dg-options "-O1 -fwrapv" } */
#include <limits.h>
void exit (int);
void abort ();
int f (int a) {
	int abs = (a > 0 ? a : -a);
	if (abs >= 0) return 1;
	else return 0;
}

int main (int argc, char *argv[]) {
	if (f(INT_MIN))
	  abort ();
	else
	  exit (0);
}
