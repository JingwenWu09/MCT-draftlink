#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern int strcmp(const char *, const char *);
extern void abort(void);
#if DEBUG
extern int puts(const char *);
#else
#define puts(X)
#endif

% : define glue(x, y) x % : % : y
#ifndef glue
#error glue not defined!
#endif
                                %
    : define str(x) %
    : x

      int main(int argc, char *argv<::>) glue(<, %)

          const char di_str glue(<,
                                 :)
              glue(
                  :, >) = str(%
                              : %
                              : <::><% %> %
                              :);

if (glue(str, cmp)(di_str, "%:%:<::><%%>%:")) {
  do {
    puts("Digraph spelling not preserved!");
    abort();
  } while (0);
}

return 0;
glue(%, >)
