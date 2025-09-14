#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  goto bug;
  switch (0) {
  bug:
    return 0;
  }
}
