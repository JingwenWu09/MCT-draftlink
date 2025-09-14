#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct A {
  int i;
  int j;
};
struct B {
  int : 4;
  int i : 12;
  int j : 12;
  int : 4;
};

__attribute__((noinline)) void bar(int x) {
  asm volatile("" : : "r"(x) : "memory");
}

__attribute__((noinline)) int f1(int k) {
  struct A a = {4, k + 6};
  asm("" : "+r"(a.i));
  a.j++;
  bar(a.i);
  bar(a.j);

  return a.i + a.j;
}

__attribute__((noinline)) int f2(int k) {
  int a[2] = {4, k + 6};
  asm("" : "+r"(a[0]));
  a[1]++;
  bar(a[0]);
  bar(a[1]);

  return a[0] + a[1];
}

__attribute__((noinline)) int f3(int k) {
  struct B a = {4, k + 6};
  asm("" : "+r"(a.i));
  a.j++;
  bar(a.i);

  bar(a.j);

  return a.i + a.j;
}

int main(void) {
  int k;
  asm("" : "=r"(k) : "0"(7));
  f1(k);
  f2(k);
  f3(k);
  return 0;
}
