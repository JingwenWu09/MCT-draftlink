#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

static union U{ 
	char buf[12 * sizeof(long long)]; 
};
union U u;

int main() {
  int off, len, i;
  char temp;
  char *p, *q = &temp;

  for (off = 0; off < (sizeof(long long)); off++) {
    for (len = 1; len < (10 * sizeof(long long)); len++) {
      for (i = 0; i < (12 * sizeof(long long)); i++) {
        u.buf[i] = 'a';
      }
      p = (__extension__(__builtin_constant_p('\0') && ('\0') == '\0' ? ({ void *__s = (u.buf + off);__s;}): __builtin_memset(u.buf + off, '\0', len)));
      if (p != u.buf + off) {
        abort();
      }
      for (i = 0; i < off; i++, q++) {
        ;
      }
      if (*q != 'a') {
        ;
      }
    }
  }
  return 0;
}
