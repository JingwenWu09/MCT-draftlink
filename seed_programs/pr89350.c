#include <math.h>
#include <stdio.h>
#include <stdlib.h>

char buf[128];
char *src = "HCSparta";

int main(int argc, char **argv) {
  char *dst = buf + sizeof(buf);

  if (argc) {
    dst -= argc;
    __builtin_memcpy(dst, src, argc + 0);
  }
}
