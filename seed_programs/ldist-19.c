#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct Foo {
  char a;
};

struct Foo x[256];

struct Baz {
  short a;
};

struct Baz y[256];

static void __attribute__((noinline, noclone)) foo() {
  int i;
  for (i = 0; i < 256; ++i) {
    x[i] = (struct Foo) {};
  }
}

static void __attribute__((noinline, noclone)) bar() {
  int i;
  for (i = 0; i < 256; ++i) {
    x[i].a = 1;
  }
}

static void __attribute__((noinline, noclone)) foobar(unsigned char c) {
  int i;
  for (i = 0; i < 256; ++i) {
    x[i].a = c;
  }
}

static void __attribute__((noinline, noclone)) foobar2(char c) {
  int i;
  for (i = 0; i < 256; ++i) {
    x[i].a = c;
  }
}



static void __attribute__((noinline, noclone)) baz() {
  int i;
  for (i = 0; i < 256; ++i) {
    y[i].a = -1;
  }
}

int main() {
  volatile int x;
  foo();
  bar();
  foobar(x);
  foobar2(x);
  baz();
  return 0;
}
