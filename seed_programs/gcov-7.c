#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <setjmp.h>

extern void abort(void);
extern void exit(int);

jmp_buf env;
int val;
int longjmp_taken;
int bar_enter, bar_exit;
int foo_enter, foo_exit;

void bar(int i) {
  bar_enter++;

  if (i == 0) {

    longjmp_taken++;
    longjmp(env, 1);
  }
  val += i + 1;
  bar_exit++;
}

void foo(int i) {
  foo_enter++;

  if (i == 1) {

    longjmp_taken++;
    longjmp(env, 2);
  }

  bar(i);

  bar(7);

  val += 16;
  foo_exit++;
}

int passed() {
  return (val == 31 && longjmp_taken == 2 && foo_enter == 3 && foo_exit == 1 && bar_enter == 3 && bar_exit == 2);
}

void leave(int i) {
  if (i == 0) {
    abort();
  }
  exit(0);
}

int main() {
  int retval;

  if ((retval = setjmp(env))) {

    val += retval;
  }

  foo(val);

  leave(passed());
}
