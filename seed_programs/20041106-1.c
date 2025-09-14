#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <unistd.h>

struct S {
  long x __attribute__((packed));
};
volatile long sink;

void foo(struct S *s) {
  sink = s->x;
}

int main() {
  size_t ps = getpagesize();
  char *ptr, *page;
  struct S *s;

  ptr = malloc(3 * ps);
  page = (char *)(((__UINTPTR_TYPE__)ptr + (ps - 1)) & -ps);
  munmap(page + ps, ps);

  s = (struct S *)(page + ps - sizeof(struct S));
  foo(s);

  return 0;
}
