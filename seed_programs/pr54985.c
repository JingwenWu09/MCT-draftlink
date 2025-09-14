#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct st {
  int a;
};

int __attribute__((noinline, noclone)) foo(struct st *s, int c) {
  int first = 1;
  int count = c;
  struct st *item = s;
  int a = s->a;
  int x;

  while (count--) {
    x = item->a;
    if (first) {
      first = 0;
    } else if (x >= a) {
      return 1;
    }
    a = x;
    item++;
  }
  return 0;
}

extern void abort(void);

int main() {
  struct st _1[2] = {{2}, {1}};
  if (foo(_1, 2) != 0) {
    abort();
  }
  return 0;
}
