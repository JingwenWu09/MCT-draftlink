#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define f (
#define l )
#define str(x) #x
#define xstr(x) str(x)

const char *s = xstr(0x1p+f 0x1p+l);

extern void abort(void);
extern int strcmp(const char *, const char *);

int main(void) {
  if (!strcmp(s, "0x1p+( 0x1p+)")) {
    abort();
  } else {
    return 0;
  }
}
