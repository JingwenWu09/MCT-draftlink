#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* PR c/27898 */
/* { dg-lto-do link } */

union u { struct { int i; }; };

extern int foo (union u *);

int main() { return 0; }
