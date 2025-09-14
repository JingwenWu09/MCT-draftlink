#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct Point{
  long int p_x, p_y;
} ;

void bar() {
}

void f(p0, p1, p2, p3, p4, p5) struct Point p0, p1, p2, p3, p4, p5;
{
  if (p0.p_x != 0 || p0.p_y != 1 || p1.p_x != -1 || p1.p_y != 0 || p2.p_x != 1 || p2.p_y != -1 || p3.p_x != -1 || p3.p_y != 1 || p4.p_x != 0 || p4.p_y != -1 || p5.p_x != 1 || p5.p_y != 0) {
    abort();
  }
}

void foo() {
  struct Point p0, p1, p2, p3, p4, p5;

  bar();

  p0.p_x = 0;
  p0.p_y = 1;

  p1.p_x = -1;
  p1.p_y = 0;

  p2.p_x = 1;
  p2.p_y = -1;

  p3.p_x = -1;
  p3.p_y = 1;

  p4.p_x = 0;
  p4.p_y = -1;

  p5.p_x = 1;
  p5.p_y = 0;

  f(p0, p1, p2, p3, p4, p5);
}

int main() {
  foo();
  exit(0);
}
