# Bug #7 in CBMC was unconfirmed as a bounding strategy related issue. It was exposed by a test case generated using dead code elimination transformation. 

```
Me:

Example 1:
#include <stdlib.h>
#include <assert.h>

struct A3 {
  char c;
  char p[];
} a3 = {'o', "wx"};

int main() {
  char ch0 = a3.p[0];
  char ch1 = a3.p[1];

  // DCE candidate: this block will never execute
  if (a3.c != 'o') {
    char dummy = a3.p[100]; // out-of-bound dead access
    (void)dummy;
  }

  char *p = &a3.p[0];
  char alt0 = *(p + 0);
  char alt1 = *(p + 1);

  assert(ch0 == alt0);
  assert(ch1 == alt1);

  if (ch0 != 'w') abort();
  if (ch1 != 'x') abort();

  return 0;
}


Example 2:
#include<assert.h>
int a[][2][4] = {[2 ... 4][0 ... 1][2 ... 3] = 1, [2] = 2, [2][0][2] = 3};
int main(void) {
	assert( sizeof(a)/(sizeof(int)*2*4) == 5);
}


In the Example 1, run  cbmc example1.c --bounds-check generate following output results:
[main.array_bounds.1] array 'a3'.a3p upper bound in a3.a3p[(signed long int)0]: FAILURE
[main.array_bounds.2] array 'a3'.a3p upper bound in a3.a3p[(signed long int)1]: FAILURE

In the Example 2, the assert() function is true, which is confirmed by compiling with gcc and clang,
while cbmc gives the FAILURE result by running cbmc example2.c.

Is it related that the size of the first dimension is omitted when the array is defined?

CBMC version: 5.88.0
Operating system: Ubuntu 22.04, macOS
Command line: cbmc example.c

```





