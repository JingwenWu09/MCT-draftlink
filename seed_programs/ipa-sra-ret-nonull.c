#include <math.h>
#include <stdio.h>
#include <stdlib.h>

volatile void *gp;
volatile void *gq;
char buf[16];

__attribute__((returns_nonnull, noinline)) static char *foo(char *p, char *q) {
  gq = q;
  gp = p;
  return q;
}

__attribute__((returns_nonnull, noinline)) static char *bar(char *p, char *q) {
  return foo(p, q) + 8;
}

__attribute__((noipa)) static char *get_charp(void) {
  return &buf[0];
}

int main() {
  char *r;
  asm volatile("" : : : "memory");
  r = bar(get_charp(), get_charp());
  return 0;
}
