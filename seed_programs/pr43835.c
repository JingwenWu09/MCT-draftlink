#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct PMC {
  unsigned flags;
};

struct Pcc_cell {
  struct PMC *p;
  long bla;
  long type;
};

extern void abort();
extern void Parrot_gc_mark_PMC_alive_fun(int *interp, struct PMC *pmc) __attribute__((noinline));

void Parrot_gc_mark_PMC_alive_fun(int *interp, struct PMC *pmc) {
  abort();
}

int main() {
  int i;
  struct Pcc_cell c;
  c.p = 0;
  c.bla = 42;
  c.type = 4;
  int *interp = &i;
  struct Pcc_cell *cp = &c;
  if (cp->type == 4 && cp->p && !(cp->p->flags & (1 << 18))) {
    Parrot_gc_mark_PMC_alive_fun(interp, cp->p);
  }
  return 0;
}
