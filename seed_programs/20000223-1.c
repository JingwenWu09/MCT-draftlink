#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void check(char const *type, int align) {
  if ((align & -align) != align) {
    abort();
  }
}

#define QUOTE_(s) #s
#define QUOTE(s) QUOTE_(s)

#define check(t) check(QUOTE(t), __alignof__(t))

struct A {
  char c;
  signed short ss;
  unsigned short us;
  signed int si;
  unsigned int ui;
  signed long sl;
  unsigned long ul;
  signed long long sll;
  unsigned long long ull;
  float f;
  double d;
  long double ld;
  void *dp;
  void (*fp)();
};

int main() {
  check(void);
  check(char);
  check(signed short);
  check(unsigned short);
  check(signed int);
  check(unsigned int);
  check(signed long);
  check(unsigned long);
  check(signed long long);
  check(unsigned long long);
  check(float);
  check(double);
  check(long double);
  check(void *);
  check(void (*)());
  check(struct A);
  return 0;
}
