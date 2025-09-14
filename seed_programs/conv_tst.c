#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define ID_1 2400000000.0
#define ID_2 1.7
#define ID_3 -1.7

unsigned ui;
int si;

conv_i1() {
}

conv_i2() {
  ui = (unsigned)ID_2;
  si = (int)ID_2;
}

conv_i3() {

  si = (int)ID_3;
}

conv_1(d) double d;
{ ui = (unsigned)d; }

double foo(u) unsigned u;
{ return u; }

main() {
  printf("%lf\n", foo(2400000000));

  conv_i1();
  printf("%lf, %u, %d\n", ID_1, ui, si);

  conv_i2();
  printf("%lf, %u, %d\n", ID_2, ui, si);

  conv_i3();
  printf("%lf, %u, %d\n", ID_3, ui, si);

  conv_1(ID_1);
  printf("%lf, %u, %d\n", ID_1, ui, si);

  conv_1(ID_2);
  printf("%lf, %u, %d\n", ID_2, ui, si);

  conv_1(ID_3);
  printf("%lf, %u, %d\n", ID_3, ui, si);
}
