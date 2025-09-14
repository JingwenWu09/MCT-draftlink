#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

int main() {
  const char *str1 = "/*";
  const char *str2 = "'";

  if (str1[0] != '/' || str1[1] != '*' || str1[2] != '\0') {
    abort();
  }

  if (str2[0] != '\'' || str2[1] != '\0') {
    abort();
  }

#if '"' != '\"'
#error
#endif

#if !'\''
#error quote
#endif

  return 0;
}
