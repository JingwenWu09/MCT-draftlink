#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* PR gcov-profile/94029 */
/* { dg-options "-ftest-coverage" } */
/* { dg-do compile } */


void test_t1() { 
}
void test_t2() { 
}

int main()
{
  test_t1();
  test_t2();
  return 0;
}

/* { dg-final { run-gcov gcov-pr94029.c } } */
