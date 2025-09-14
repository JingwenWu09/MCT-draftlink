# Bug #15 in SeaHorn was confirmed as a C standard library issue. It was exposed by a test case generated using constant propagation transformation.

```
Me:

#include  "seahorn/seahorn.h"
#include <string.h>

void foo(char *p, unsigned long *r) {
  unsigned long n0 = strlen(p);
  *r = n0;
}

void foo_orig(char *p, unsigned long *r) {
  *r = strlen(p);
}

void foo_cp(char *p, unsigned long *r, unsigned long precomputed_len) {
  *r = precomputed_len;
}

int main() {
  char a[4] = "";
  strcpy(a, "123");

  unsigned long r1, r2;
  foo_orig(a, &r1);
  foo_cp(a, &r2, strlen(a));
  sassert(r1 == r2);  

  return 0;
}

In this case, I run sea bpf -m64 --promote-nondet-undef=false example.c.
I don't understand why SeaHorn gave the unexpected result sat. Is it related to the pointer parameter of function foo()?

```
```
Developer:

Btw, I should have said that you should run bpf --bmc=opsem which runs the bounded model checker
but with a more precise modeling of the C semantics.

In any case, if you run sea bpf --bmc=opsem  --promote-nondet-undef=false ../test_505.c

Warning: unhandled instruction:   %_2 = call i32 @strlen(i8* noundef nonnull dereferenceable(1) %_1) #8 @ entry in main (BvOpSem2.cc:3433)

so strlen is treated as a nondeterministic call which is not precise enough to prove your assertion.


```



