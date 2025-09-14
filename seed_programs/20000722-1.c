#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct s {
  char *p;
  int t;
};

extern void bar(void);
extern void foo(struct s *);

int main(void) {
  bar();
  bar();
  exit(0);
}

void bar(void) {
	struct s temp = {"hi", 1};
  struct s *p = &temp;
  if (p->t != 1) {
    abort();
  }
  p->t = 2;
}
