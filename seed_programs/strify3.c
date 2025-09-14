#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern int strcmp(const char *, const char *);
#if DEBUG
extern int puts(const char *);
#else
#define puts(X)
#endif
extern void abort(void);

#define str(x) #x
#define xstr(x) str(x)
#define glibc_hack(x, y) x @y

int main(int argc, char *argv[]) {

  char a[] = xstr(glibc_hack(foo, bar));

  if (strcmp(a, "foo@bar")) {
    do {
      puts("stringification without spaces");
      abort();
    } while (0);
  }

  return 0;
}
