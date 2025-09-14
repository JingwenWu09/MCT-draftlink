#include <math.h>
#include <stdio.h>
#include <stdlib.h>
typedef __SIZE_TYPE__ size_t;
extern void *memcpy(void *__restrict, const void *__restrict, size_t);
extern void abort(void);
extern void exit(int);

struct t {
  unsigned a : 16;
  unsigned b : 8;
  unsigned c : 8;
  long d[4];
};
struct t t = {26, 0, 0, {0, 21, 22, 23}};

struct U{
  long r[3];
};

struct t * bar(struct U, unsigned int);

struct t * foo(struct t * x) {
  struct U d, u;

  memcpy(&u, &x->d[1], sizeof u);
  d = u;
  return bar(d, x->b);
}

struct t * baz(struct t * x) {
  struct U d, u;

  d.r[0] = 0x123456789;
  d.r[1] = 0xfedcba987;
  d.r[2] = 0xabcdef123;
  memcpy(&u, &x->d[1], sizeof u);
  d = u;
  return bar(d, x->b);
}

struct t * bar(struct U d, unsigned int m) {
  if (d.r[0] != 21 || d.r[1] != 22 || d.r[2] != 23) {
    abort();
  }
  return 0;
}

int main(void) {
  baz(&t);
  foo(&t);
  exit(0);
}
