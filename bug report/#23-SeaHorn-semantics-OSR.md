# Bug #12 in SeaHorn was confirmed as a language semantics related issue. It was exposed by a test case generated using operator strength reduction transformation.

```
Me:

#include "seahorn/seahorn.h"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

static unsigned int a[256], b = 0;
static char c = 0;
static int d = 0;
static long long e = 0;

static short foo(long long x, long long y) {
  int m = 2;
  int n = 2;
  for(int i=0; i<1; i++) m--;
  for(int i=0; i<1; i++) n--;
  m = m << 32;
  n = n / 4294967296 UL;
  sassert(m == n);	
  return (x - m) / (y - n);
}

static char bar(char x, char y) {
  return x - y;
}

static int baz(int x, int y) {
  static int *f = &d;
  *f = (y != (short)(y * 3));
  for (c = 0; c < 2; c++) {
    do {
      if (d) {
        if (e) {
          e = 1;
        } else {
          return x;
        }
      } else {
        d = 1;
      }
    } while (!d);
    f = &d;
  }
  return x;
}

static void fnx(unsigned long long x, int y) {
  if (!y) {
    b = a[b & 1];
    b = a[b & 1];
    b = a[(b ^ (x & 1)) & 1];
    b = a[(b ^ (x & 1)) & 1];
  }
}

int main() {
  char *volatile w = "2";

  int h = 0;
  unsigned int k = 0;
  int l[8];

  if (__builtin_strcmp(w, "1") == 0) {
    h = 1;  // dead branch under w = "2"
  }

  for (int i = 0; i < 256; i++) {
    for (int j = 8; j > 0; j--) {
      k = 1;
    }
    a[i] = k;
  }

  for (int i = 0; i < 8; i++) {
    l[i] = 0;
  }

  d = bar(c, c); 
  d = baz(c, 1 | foo(l[0], 10)); 
  fnx(d, h); 
  fnx(e, h); 

  if (d != 0) {
    __builtin_abort();
  }
  return 0;
}

In this example, seahorn gives sat result.
Does seahorn have any special treatment for data truncation?
Does seahorn give the sat result if there is some security issue with the program (e.g., if the length of the shift left is greater than the number of bytes of the data type)?
That is, sat is not only for __VERIFIER_error.
Whether some property checks can be ignored by modifying command line directives?
```
```
Developer:

I think in both cases LLVM simplifies the code too aggressively because it detects undefined behavior (UB).
Try to run clang followed by opt with -O3 to see what LLVM produces.
Definitely in the first example it can know that x and y are 1 after the loops because it can unroll those.
Then, it knows that if x and y are integers of 32 bits then the shift 1 << 32 will overflow
and because signed integer overflow is UB then it will remove basically all the code.
```
```
Me:

Can I take it that seahorn gives sat results for UB that exists after clang processing?
```
```
Developer:

It is a little more nuanced than that. By default, SeaHorn runs after clang optimizations.
Clang optimizations preserve behavior only if the code has no UB. There are no guarantees for code that has UB.

SeaHorn will return sat if the input given to it (i.e., after clang and optimization) has an execution that reaches __VERIFIER_error.
If the original input file has UB, the input to SeaHorn might or might not contain an execution that reaches __VERIFIER_error, independently of whether original file had such execution.

In summary, SeaHorn assumes that the input is a well-defined C program that has well-defined semantics
and can be compiled to LLVM bitcode. SeaHorn analyses the bitcode, not the input C programs.
While it is possible to use SeaHorn to detect UB (for example, we use it to detect buffer overflow),
it is not easy and has to be carefully designed to be sound.
```



