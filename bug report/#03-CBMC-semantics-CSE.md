# Bug #3 in CBMC was confirmed as a language semantics related issue. It was exposed by a test case generated using common subexpression elimination transformation.

```
Me:

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

enum e1 { c1 = -__INT_MAX__ };

__attribute__((noinline, noclone)) int f(enum e1 *p, short *q) {
  int v1 = ((*p ^ 0xFF) + *q) * 3;
  int v2 = ((*p ^ 0xFF) + *q) * 5;

  int tmp = (*p ^ 0xFF) + *q;
  int v1_cse = tmp * 3;
  int v2_cse = tmp * 5;

  assert(v1 == v1_cse);
  assert(v2 == v2_cse);
  return v1 + v2;
}

int main() {
  short x = 42;
  enum e1 *a = (enum e1 *)&x;
  short *b = &x;
  f(a, b);
  return 0;
}

CBMC version: 5.88.0
Operating system: Ubuntu 22.04, macOS
Command line: cbmc example.c --pointer-check

What happened:

line 15 dereference failure: pointer outside object bounds in *p: FAILURE
line 17 dereference failure: pointer outside object bounds in *p: FAILURE
line 23 dereference failure: pointer outside object bounds in *p: FAILURE
line 25 dereference failure: pointer outside object bounds in *p: FAILURE

For *p, why do function g and function h have the above failure, but function f does not？
Please help me to explain, thanks.
```
```
Developer:

According to the C standard section 6.7.2.2.2 - "The expression that defines the value of an enumeration
constant shall be an integer constant expression that has a value representable as an int."
When analysing C code, cbmc currently models the type underlying an enum as being a signed int.
If you form a pointer to a short or a char then those pointers may point to a region of memory which is
smaller than the memory allocated to an enum value. Therefore if you cast these pointers to a pointer to enum type
and dereference them, then you are writing to or reading from memory beyond the memory allocated to
the expected destination of the pointer. This problem is then reported as a dereference failure in the output of cbmc.
```



