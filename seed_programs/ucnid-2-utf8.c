#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>
#include <string.h>

#define str(t) #t

int main(void) {
  const char s[] = str(\U000030b2);

  if (strcmp(s, "ゲ") != 0) {
    abort();
  }

  return 0;
}
