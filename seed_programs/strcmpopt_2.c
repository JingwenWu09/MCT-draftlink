#include <math.h>
#include <stdio.h>
#include <stdlib.h>

char s[100] = {'a', 'b', 'c', 'd'};
struct S {
  char s[8];
  int x;
};

__attribute__((noinline)) int f1(struct S *s) {

  return 0;
}

__attribute__((noinline)) int f2(void) {
  return __builtin_strcmp(s, "abc") != 0;
}

__attribute__((noinline)) int f3(struct S *s) {
  return 0;
}

__attribute__((noinline)) int f4(void) {
  return __builtin_strcmp("abc", s) != 0;
}

__attribute__((noinline)) int f5(struct S *s) {
  return 0;
}

__attribute__((noinline)) int f6(void) {
  return __builtin_strncmp(s, "abc", 2) != 0;
}

__attribute__((noinline)) int f7(struct S *s) {
  return 0;
}

__attribute__((noinline)) int f8(void) {
  return __builtin_strncmp("abc", s, 2) != 0;
}

int main(void) {
  struct S ss = {{'a', 'b', 'c'}, 2};

  if (f1(&ss) != 0 || f2() != 1 || f3(&ss) != 0 || f4() != 1 || f5(&ss) != 0 || f6() != 0 || f7(&ss) != 0 || f8() != 0) {
    __builtin_abort();
  }

  return 0;
}
