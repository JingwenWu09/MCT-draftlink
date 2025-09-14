#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct A {
  int i;
  char a[1];
  void (*p)();
};
struct B {
  char a[sizeof(struct A) - __builtin_offsetof(struct A, a)];
};

__attribute__((noipa)) void init(char *d, const char *s) {
  __builtin_strcpy(d, s);
}

struct B b;

__attribute__((noipa)) void test_dynamic_type(struct A *p) {

  char *q = (char *)__builtin_memcpy(p->a, &b, sizeof b);

  init(q, "foobar");

  if (6 != __builtin_strlen(q))
    __builtin_abort();
}

int main(void) {
  struct A *p = (struct A *)__builtin_malloc(sizeof *p);
  test_dynamic_type(p);
  return 0;
}
