#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

struct Foo {
  int a;
  int b;
};

struct Foo foo(struct Foo first, struct Foo last, _Bool ret_first) {
  struct Foo t;
  struct Foo *t1 = (ret_first ? &first : &last);
  first.a = 2;
  last.b = 3;
  t.a = t1->a;
  t.b = t1->b;
  t.a += first.a;
  t.b += last.b;
  return t;
}

int main() {
  struct Foo first = (struct Foo){1, 2};
  struct Foo last = (struct Foo){3, 4};
  struct Foo ret = foo(first, last, 0);
  if (ret.b != 6) {
    abort();
  }
  return 0;
}
