#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do compile } */
/* { dg-options "-O -Wunused-but-set-variable" } */

int main(void)
{
	const int i = 0;
	switch(i)
	{
	default: break;
	}
}


