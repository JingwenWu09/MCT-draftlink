# Bug #2 in CBMC was confirmed as a C standard library related issue. It was exposed by a test case generated using operator strength reduction transformation. 

```
Me:

#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

__attribute__((noipa)) void foo(unsigned char *buf, unsigned char *tab) {
  tab = __builtin_assume_aligned(tab, 2);
  buf = __builtin_assume_aligned(buf, 2);

  unsigned v_orig = tab[1] ^ (tab[0] * 256);

  // Operator Strength Reduction version
  unsigned v_osr = tab[1] ^ (tab[0] << 8);

  assert(v_orig == v_osr);

  buf[0] = ~(v_osr >> 8);
  buf[1] = ~v_osr;
}

int main() {
  volatile unsigned char l1 = 0;
  volatile unsigned char l2 = 1;
  unsigned char buf[2] __attribute__((aligned(2)));
  unsigned char tab[2] __attribute__((aligned(2))) = {l1 + 1, l2 * 2};
  foo(buf, tab);
  assert(buf[0] == (unsigned char)~1);
  assert(buf[1] == (unsigned char)~2);
  return 0;
}

CBMC gives the FAILURE result towards assert(buf[0] == (unsigned char)~1) and assert(buf[1] == (unsigned char)~2).
Does cbmc not support certain built-in functions？

CBMC version: 5.88.0
Operating system: Ubuntu 22.04, macOS
Command line: cbmc example.c
```
```
Developer:

We support a number of different attributes but not noipa.
```



