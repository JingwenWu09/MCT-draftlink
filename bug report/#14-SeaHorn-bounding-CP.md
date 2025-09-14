# Bug #3 in SeaHorn was fixed as a bounding strategy related issue. It was exposed by a test case generated using constant propagation transformation. 

```
Me:

#include "seahorn/seahorn.h"
int main() {
  int left_DjT[20], left_DjT1[20];
  while (k < 20) {
    k++;
    if (j > i) {
      j = 5;
    } else {
      j = 3;
    }
  }
  for (int i = 0; i < k; i++) {
    left_DjT[i] = 42;
  }
  for (int i = 0; i < 20; i++) {
    left_DjT1[i] = 42;
  }
  for (int x = 0; x < 20; x++) {
    sassert(left_DjT[x] == left_DjT1[x]);
  }
  return 0;
}

In this example, SeaHorn produces a sat result. However, if the sizes of left_DjT and left_DjT1 are reduced to fewer than 10 elements, the result becomes unsat.

Version: seahorn 14.0.0
OS: ubuntu 22.04
Command line: sea pf -m64 example.c
```
```
Developer:

In both cases, if you don't use the option -m64 the result you get is "unsat".
Note by the way that sea pf in this case unrolls the loops so
if you keep increasing the size of the array you will get longer verification times.
You can see the verification results by using the option --show-invars.
However, when you add the option -m64 then sea pf gets "sat" but consistently in both cases (N=10 and N=20).
The reason is that with this option the compiler seems to vectorize the loop
and sea pf currently ignores some of the instructions (e.g., extractelement).
Alternatively, you can use the BMC engine using the command sea bpf --m64 --bmc=opsem
and you will get also the expected results .
```
```
Developer:

There was a bug that did not disable vectorization by default. This is fixed in commit 8bfaa64.
```



