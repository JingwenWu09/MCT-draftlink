#include <limits.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

#if INT_MAX == 32767
#define LONG_CHARCONST '!\234a'
#define SHORT_CHARCONST '\234a'
#define POS_CHARCONST '\1'
#elif INT_MAX == 2147483647
#define LONG_CHARCONST '!\234abc'
#define SHORT_CHARCONST '\234abc'
#define POS_CHARCONST '\234a'
#elif INT_MAX == 9223372036854775807
#define LONG_CHARCONST '!\234abcdefg'
#define SHORT_CHARCONST '\234abcdefg'
#define POS_CHARCONST '\234a'
#else

#define LONG_CHARCONST '\234a'
#define SHORT_CHARCONST '\234a'
#define POS_CHARCONST '\1'
#endif

#if POS_CHARCONST < 0
#error Charconst incorrectly sign-extended
#endif

#if LONG_CHARCONST != SHORT_CHARCONST
#error Overly long charconst truncates wrongly for preprocessor
#endif

int main() {
  if (POS_CHARCONST < 0) {
    abort();
  }
  if (LONG_CHARCONST != SHORT_CHARCONST) {
    abort();
  }
  return 0;
}
