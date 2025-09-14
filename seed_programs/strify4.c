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

#define S(X) S2(X)
#define S2(X) #X
#define TAB "	"

int main(int argc, char *argv[]) {

  char a[] = S(S(TAB));

  if (strcmp(a, "\"\\\"	\\\"\"")) {
    do {
      puts("stringification caused octal");
      abort();
    } while (0);
  }

  return 0;
}
