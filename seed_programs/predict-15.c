#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
  if (argc == 123) {
    goto exit;
  } else {
    return 0;
  }

exit:
  return 1;
}
