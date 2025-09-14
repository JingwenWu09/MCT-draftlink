# Bug #1 in CBMC was confirmed as a language semantics related issue. It was exposed by a test case generated using if-else chain to switch conversion transformation. 

```
Me:

#include <assert.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#if __SIZEOF_SHORT__ == 2 && __SIZEOF_INT__ == 4 && __CHAR_BIT__ == 8
struct A {
  unsigned int a, b, c;
  unsigned int d : 30;
  unsigned int e : 2;
};

union U {
  struct A f;
  unsigned int g[4];
  unsigned short h[8];
  unsigned char i[16];
};
volatile union U v = {.f.d = 0x4089};
volatile union U *v_global_p = &v; //globalVarAnnotation

__attribute__((noipa)) void bar(int x) {
  int i=0;
//storeGlobalVarStmt
volatile union U v_store = *v_global_p;
  //renameUseVarStmt
  int i_1 = i;
  int x_1 = x;
  if ((i++) == 0) {
  if (x != v.f.d) {
    __builtin_abort();
  }
} else if ((i++) == 1) {
  if (x != v.f.e) {
    __builtin_abort();
  }
} else if ((i++) == 2) {
  if (x != v.g[3]) {
    __builtin_abort();
  }
} else if ((i++) == 3) {
  if (x != v.h[6]) {
    __builtin_abort();
  }
} else if ((i++) == 4) {
  if (x != v.h[7]) {
    __builtin_abort();
  }
} else {
  __builtin_abort();
}

// restore global
*v_global_p = v_store;

// use backup variable in switch
switch (i_1++) {
  case 0:
    if (x_1 != v.f.d) {
      __builtin_abort();
    }
    break;
  case 1:
    if (x_1 != v.f.e) {
      __builtin_abort();
    }
    break;
  case 2:
    if (x_1 != v.g[3]) {
      __builtin_abort();
    }
    break;
  case 3:
    if (x_1 != v.h[6]) {
      __builtin_abort();
    }
    break;
  case 4:
    if (x_1 != v.h[7]) {
      __builtin_abort();
    }
    break;
  default:
    __builtin_abort();
    break;
}
  // conditionStmt
assert(i == i_1);
assert(x == x_1);

// pointerDiffStmt
assert(labs((char*)&v.h[7] - (char*)&v.h[6]) >= 0);

// abs-related assert
assert(abs(i - i_1) == 0);
assert(abs(x - x_1) >= 0);
}

void foo(unsigned int x) {
  union U u;
  u.f.d = x >> 2;
  u.f.e = 0;
  bar(u.f.d);
  bar(u.f.e);
  bar(u.g[3]);
  bar(u.h[6]);
  bar(u.h[7]);
}

int main() {
  foo(0x10224);
  return 0;
}
#else
int main() {
  return 0;
}
#endif


The assert() functions are true, which is confirmed by compiling with gcc and clang, while cbmc
gives the FAILURE result towards assert(labs((char*)&v.h[7] - (char*)&v.h[6]) >= 0) and assert(abs(x - x_1) >= 0)
which means the assertions are false. abs(i) is always true, why do assertions fail?

CBMC version: 5.88.0
Operating system: Ubuntu 22.04, macOS
Command line: cbmc example.c
```
```
Developer:

Unless I'm mistaken, your assertions shouldn't be holding - you're invoking undefined behaviour.

According to the C standard n1570-6.5.9:

When two pointers are subtracted, both shall point to elements of the same array object,
or one past the last element of the array object; the result is the difference of the
subscripts of the two array elements. The size of the result is implementation-defined,
and its type (a signed integer type) is ptrdiff_t defined in the <stddef.h> header.
If the result is not representable in an object of that type, the behavior is undefined. In
other words, if the expressions P and Q point to, respectively, the i-th and j-th elements of
an array object, the expression (P)-(Q) has the value i−j provided the value fits in an
object of type ptrdiff_t. Moreover, if the expression P points either to an element of
an array object or one past the last element of an array object, and the expression Q points
to the last element of the same array object, the expression ((Q)+1)-(P) has the same
value as ((Q)-(P))+1 and as -((P)-((Q)+1)), and has the value zero if the
expression P points one past the last element of the array object, even though the
expression (Q)+1 does not point to an element of the array object.

You're also invoking labs(long i) with a ptrdiff_t argument, which may or may not be automatically
promoted to long depending on the platform definitions in stddef.h.
```
```
Developer:

abs(i) >= 0 is not always true. The absolute value of the most-negative value is out of range.
That is, abs(-2147483648) is 2147483648 which is out of the range of int.
```
```
Developer:

Since abs(INT_MIN) is undefined behaviour, we should add an assertion to the library model.
```



