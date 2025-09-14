# Bug #10 in SeaHorn was confirmed as a C standard library related issue. It was exposed by a test case generated using operator strength reduction transformation.
```
Me:

#include "seahorn/seahorn.h"
#include <math.h>
#include <stdlib.h>

typedef unsigned long T;
#define M (sizeof(T) * 4)

struct S {
  T a, b;
  unsigned char c, d;
};

struct S foo(T x, T y) {
  struct S e;
  T f[2], g;

  e.b = (x & (~(T)0 >> M)) * (y & (~(T)0 >> M));
  e.a = (x >> M) * (y >> M);

  f[0] = (x & (~(T)0 >> M)) * (y >> M);
  f[1] = (x >> M) * (y & (~(T)0 >> M));

  g = e.b;
  e.b += (f[0] & (~(T)0 >> M)) << M;
  if (e.b < g) {
    e.a++;
  }

  g = e.b;
  e.b += (f[1] & (~(T)0 >> M)) << M;
  if (e.b < g) {
    e.a++;
  }

  e.a += (f[0] >> M);
  e.a += (f[1] >> M);
  e.c = 1;
  e.d = 0;
  return e;
}

int main() {
  T x = 1UL << (M * 2 - 1);
  struct S y = foo(1, x);

  int base = 2;
  int exponent = 4;

  if (y.b % 2 == 0) {
    base = 2;
  }

  if (y.a > 0) {
    exponent = 4;
  }

  double d0 = fabs((double)(-1 * base * base * base * base)); 
  double d1 = pow((double)base, (double)exponent); // d1 = 16.0
  
  sassert(d0 == d1);

  if (y.a || y.b != x || y.c != 1 || y.d) {
    abort(); 
  }

  return 0;
}

In this example, the command line prints the following warning information:

WARNING: no assertion was found so either program does not have assertions or frontend discharged them.
but the example does contain the sassert statement, please help me to explain why the warning occurs
or does this warning mean something else?

```
```
Developer:

Front end figures out that 2^2 = 4 and solves the assertions.
By the time the code gets to seahorn engine there is no assertion anymore.
Sorry for the confusing warning.
```



