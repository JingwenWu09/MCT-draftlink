# Bug #9 in CBMC was unconfirmed as a pointer alias related issue. It was exposed by a test case generated using common subexpression elimination transformation.

Me:

```
#include <math.h>
#include <stdlib.h>
#include <assert.h>
#include <stddef.h>
#include <stdio.h>
#include <sys/mman.h>

#ifndef MAP_ANONYMOUS
#define MAP_ANONYMOUS MAP_ANON
#endif
#ifndef MAP_ANON
#define MAP_ANON 0
#endif
#ifndef MAP_FAILED
#define MAP_FAILED ((void *)-1)
#endif

__attribute__((noipa)) void foo(char *p, char *q, int r) {
  // -------- Original control-flow expressions --------
  int cond1_orig = (q[0] == 'a' && q[1] == 'b');
  int cond2_orig = !r && (q[0] == 'a' && q[1] == 'b');
  int cond3_orig = (q[0] == 'a' && q[1] == 'b') || (q[0] == 'x' && q[1] == 'y');

  // -------- Transformed version (CSE) --------
  char c0 = q[0];
  char c1 = q[1];

  int cond_base = (c0 == 'a' && c1 == 'b');
  int cond_alt = (c0 == 'x' && c1 == 'y');

  int cond1_new = cond_base;
  int cond2_new = !r && cond_base;
  int cond3_new = cond_base || cond_alt;

  // -------- Assert semantic equivalence --------
  assert(cond1_orig == cond1_new);
  assert(cond2_orig == cond2_new);
  assert(cond3_orig == cond3_new);

  // -------- Use transformed values --------
  if (cond2_new) {
    p[0] = c0;
    p[1] = c1;
  } else if (cond_alt) {
    p[0] = '!';
    p[1] = '!';
  } else {
    p[0] = '?';
    p[1] = '?';
  }

  // Final semantic validation
  if (cond_base) {
    assert(p[0] == 'a' && p[1] == 'b');
  } else if (cond_alt) {
    assert(p[0] == '!' && p[1] == '!');
  } else {
    assert(p[0] == '?' && p[1] == '?');
  }
}

int main() {
  char *p = mmap(NULL, 131072, PROT_READ | PROT_WRITE,
                 MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
  if (p == MAP_FAILED) return 0;

  if (munmap(p + 65536, 65536) < 0) return 0;

  // ✏️ Setup test case with 'a', 'b' at end of first page
  p[65536 - 2] = 'a';
  p[65536 - 1] = 'b';

  // Overwrite initial values (will be replaced if condition matches)
  p[0] = 'X';
  p[1] = 'Y';

  volatile int r = 0;
  foo(p, p + 65536 - 2, r);

  return 0;
}
```

CBMC version: 5.88.0
Operating system: Ubuntu 22.04, MacOS
command line: cbmc example.c --pointer-check

What happened:
```
line 18 dereference failure: deallocated dynamic object in q[(signed long int)0]: FAILURE
line 27 dereference failure: deallocated dynamic object in p[(signed long int)0]: FAILURE
```
But if you comment out the following code in the main function, there are no such FAILURE.
```
if (munmap(p + 65536, 65536) < 0) {
    return 0;
}
```
