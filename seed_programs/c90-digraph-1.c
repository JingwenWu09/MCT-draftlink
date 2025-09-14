#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define str(x) xstr(x)
#define xstr(x) #x
#define foo(p, q) str(p % : % : q)

extern void abort(void);
extern int strcmp(const char *, const char *);

int main(void) {
  const char *t = foo(1, 2);
  const char *u = str(< :);
  if (!strcmp(t, "1 %:%: 2") && !strcmp(u, "<:")) {
    abort();
  } else {
    return 0;
  }
}
