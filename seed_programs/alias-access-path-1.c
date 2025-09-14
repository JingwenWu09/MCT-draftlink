#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct a {
  int val;
} a = {1}, a2;
struct b {
  struct a a;
};
int val;
struct c {
  struct b b;
} *cptr = (void *)&val;

int main(void) {
  cptr->b.a = a;
  val = 2;
  a2 = cptr->b.a;
  if (a2.val == a.val) {
    __builtin_abort();
  }
}
