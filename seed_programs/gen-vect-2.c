#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>

#define N 16

#if __INT_MAX__ == 32767
typedef char half_word;
#elif __LONG_MAX__ == 2147483647
typedef short half_word;
#else
typedef int half_word;
#endif

half_word cb[N] = {0, 3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 45};

int main() {
  half_word ca[N];
  int i;

  for (i = 0; i < N; i++) {
    ca[i] = cb[i];
  }

  for (i = 0; i < N; i++) {
    if (ca[i] != cb[i]) {
      abort();
    }
  }

  return 0;
}
