# Bug #4 in SeaHorn was fixed as a bounding strategy related issue. It was exposed by a test case generated using operator strength reduction transformation.

```
Me:

#include "seahorn/seahorn.h"

int main(){
  int x = 0;
  int y = 0;

  for(int i=0; i<10; i++){
    x += i * 8; 
  }
  for(int i=0; i<10; i++){
    y += i << 3; 
  }

  sassert(x == y);
  return 0;
}

In this example, why seahorn gives the "sat" result? Thanks.

Version: seahorn 14.0.0
OS: ubuntu 22.04
Command line: sea pf -m64 example.c
```
```
Developer:

There was a bug that did not disable vectorization by default. This is fixed in above commit.

Note that SeaHorn is not great at this kind of a problem. It will not learn a quantified invariant by default.
So it will have to enumerate every array entry. This, of course, requires unrolling the array.

Moreover, SeaHorn is pretty bad for arrays with fixed bound. These are unrolled (either statically or dynamically).
It is better to abstract exact number of iterations by a symbolic constant.
```



