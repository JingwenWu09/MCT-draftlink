#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do compile } */

int __attribute__((optimize("no-lto"))) main(void){return 0;} /* { dg-warning "bad option" } */
