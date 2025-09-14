#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <setjmp.h>
#include <stdbool.h>

static jmp_buf buf;
static _Bool stop = false;

void call_func(void (*func)(void)) {
  func();
}

void func(void) {
  stop = true;
  longjmp(buf, 1);
}

int main(void) {
  setjmp(buf);

  while (!stop) {
    call_func(func);
  }

  return 0;
}
