#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdnoreturn.h>

extern int strcmp(const char *, const char *);

noreturn void exit(int);
noreturn void abort(void);

noreturn int f1(void);

noreturn void f2(void);

static void noreturn f3(void) {
  exit(0);
}

noreturn int f4(void) {
  return 1;
}

noreturn void f5(void) {
  return;
}

noreturn void f6(void) {
}

noreturn void f7(int a) {
  if (a) {
    exit(0);
  }
}

void f2(void);

void f8(void);
noreturn void f8(void);

noreturn noreturn void noreturn f9(void);

void (*fp)(void) = f5;

#ifndef noreturn
#error "noreturn not defined"
#endif

#define str(x) #x
#define xstr(x) str(x)

const char *s = xstr(noreturn);

int main(void) {
  if (strcmp(s, "_Noreturn") != 0) {
    abort();
  }
  exit(0);
}
