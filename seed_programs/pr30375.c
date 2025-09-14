#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct s {
  int a;
  int b;
  int c;
  int d;
};

extern void abort(void);

char *c = (void *)0;
void __attribute__((noinline)) f(void) {
  if (c) {
    *c = 1;
  }
}

void test_signed_msg_encoding(void) {
  struct s signInfo = {sizeof(signInfo), 0};

  signInfo.b = 1;
  signInfo.c = 0;
  struct s *p = &signInfo;
  if (p->d != 0) {
    abort();
  }
  signInfo.d = 1;
  f();
}

int main() {
  test_signed_msg_encoding();
  test_signed_msg_encoding();
  return 0;
}
