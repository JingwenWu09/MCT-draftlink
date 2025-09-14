#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct foo {
  char a;
} foo[100];

main() {
  foo[1].a = '1';
  foo[2].a = '2';
}
