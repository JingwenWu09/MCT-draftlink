#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

static int a;

int main(void) {
  char *src = NULL;
  char buf[128];

  switch (a) {
  case 1:
    strcpy(buf, src);
    break;
  case 0:
    strcpy(buf, "hello");
  }
  printf("%s\n", buf);
}
