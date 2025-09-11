# Bug #11 in CBMC was confirmed as a C standard library related issue. It was exposed by a test case generated using operator strength reduction transformation.

Me:

```
#include <assert.h>

struct S1 {
  int a : 2;
  __attribute__((aligned(16))) int b : 32;
  int c : 32;
  __attribute__((aligned(8))) int d : 2;
  int e : 30;
  __attribute__((aligned(16))) int f : 2;
  __attribute__((aligned(8))) int g : 2;
};

struct S1 s1;

__attribute__((noipa)) int foo(int x) {
  int sum = 0;
  sum += s1.b * 8;
  sum += s1.c / 4;
  sum += s1.e % 16;
  sum += x * 32;
  sum += (x % 2) * s1.d;
  sum += sizeof(s1) * 2;
  return sum;
}

int main() {
  s1.b = 5;
  s1.c = 33;
  s1.d = 1;
  s1.e = 123;

  int x = 7;
  int res1 = foo(x);

  // Transformed version (CSE + OSR)
  int res2 = 0;
  res2 += s1.b << 3;             // *8 → <<3
  res2 += s1.c >> 2;             // /4 → >>2
  res2 += s1.e & 15;             // %16 → &15
  res2 += x << 5;                // *32 → <<5
  res2 += (x & 1) * s1.d;        // %2 → &1
  res2 += sizeof(s1) << 1;

  assert(res1 == res2);
  assert(sizeof(s1) == 48);

  return 0;
}
```

In this example, cbmc gives the FAILURE result toward assert(sizeof(s1) == 48). 

CBMC version: 5.88.0
Operating system: Ubuntu 22.04, MacOS
command line: cbmc example.c
