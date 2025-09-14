#include <math.h>
#include <stdio.h>
#include <stdlib.h>
unsigned int UuT(void) {
  unsigned int true_var = 1;
  unsigned int false_var = 0;
  unsigned int ret = 0;

  if (true_var) {
    if (false_var) {
      ret = 111;
    }
  } else {
    ret = 999;
  }
  return ret;
}

int main(int argc, char **argv) {
  UuT();
  return 0;
}
