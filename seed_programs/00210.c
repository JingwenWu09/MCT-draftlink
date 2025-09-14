#include <math.h>
#include <stdio.h>
#include <stdlib.h>
typedef unsigned short uint16_t;
typedef unsigned char uint8_t;

typedef union Unaligned16a {
  uint16_t u;
  uint8_t b[2];
} __attribute__((packed)) Unaligned16a;

typedef union __attribute__((packed)) Unaligned16b {
  uint16_t u;
  uint8_t b[2];
} Unaligned16b;

extern void foo(void) __attribute__((stdcall));
void __attribute__((stdcall)) foo(void) {
}

#define ATTR __attribute__((__noinline__))
int ATTR actual_function() {
  return 42;
}

extern int printf(const char *, ...);
int main() {
  void *function_pointer = &actual_function;

  int a = ((ATTR int (*)(void))function_pointer)();
  printf("%i\n", a);

  int b = ((int(ATTR *)(void))function_pointer)();
  printf("%i\n", b);

  return 0;
}
