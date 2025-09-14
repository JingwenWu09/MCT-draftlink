#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void _cairo_clip_path_reference() {
  int a;
  __sync_fetch_and_add(&a, 1);
}

int main(void) {
  return 0;
}
