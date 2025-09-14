#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

typedef long long V __attribute__((vector_size(2 * sizeof(long long)), may_alias));

struct P {
  V b;
} __attribute__((aligned(1)));

struct __attribute__((packed)) T {
  char c;
  struct P s;
};

__attribute__((noinline, noclone)) void foo(struct P *p) {
  p->b[1] = 5;
}

int main() {
  V a = {3, 4};
  struct T t;

  t.s.b = a;
  foo(&t.s);

  if (t.s.b[0] != 3 || t.s.b[1] != 5) {
    abort();
  }

  return 0;
}
