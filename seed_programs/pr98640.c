#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdint.h>

uint64_t var_0 = 18128133247277979402ULL;
int64_t var_14 = 6557021550272328915LL;
uint64_t var_83 = 10966786425750692026ULL;

void test() {
  var_14 = var_0 + (_Bool)7;
  var_83 = 1 + (int)var_0;
}

int main() {
  test();
  if (var_83 != 888395531) {
    __builtin_abort();
  }
  return 0;
}
