#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdio.h>

static int __attribute__((no_icf)) foo() {
  return 2;
}

static int __attribute__((no_icf)) bar() {
  return 2;
}

int main() {
  return foo() - bar();
}
