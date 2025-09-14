#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct ge {
  char au;
  char pz[];
};

struct ge tr = {
    'X',
    'X',
};

int main(void) {
  return tr.pz[0] == 'X';
}
