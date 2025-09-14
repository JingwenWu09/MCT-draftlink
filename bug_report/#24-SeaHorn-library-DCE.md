# Bug #13 in SeaHorn was confirmed as a C standard library related issue. It was exposed by a test case generated using dead code elimination transformation.

```
Me:

#include <string.h>
#include "seahorn/seahorn.h"

#define N 50
int a[N];
int b[N];
int st = 1;

void foo(int arr[]){
  int i;
  for (i = 0; i < 50; i++) {
    if ((i & 0xFFFF0000) != 0) break;
    if (i < 25) {
      arr[i] = i;
    }
    arr[50 - i] = 1;
  }

  int res = 0;
  for (i = 0; i < 50; i += st) {
    res += arr[i];
  }
  if(res != 49) abort(); 
}

void foo1(int arr[]){
  int i;
  for (i = 0; i < 50; i++) {
    if (i < 25) {
      arr[i] = i;
    }
    arr[50 - i] = 1;
  }

  int res = 0;
  for (i = 0; i < 50; i += st) {
    res += arr[i];
  }
  if(res != 49) abort(); 
}

int main(){
  init(a);
  init(b);
  sassert(memcmp(a, b, sizeof(a)) == 0);
  return 0;
}

In this example, the assertion memcmp(a, b, sizeof(a)) == 0 should be true,
seahorn gives sat result with the commands sea bpf -m64 --bmc=opsem example.c.
Does seahorn support memcmp function? Does it support some other memory manipulation functions,
such as memset, memcpy, memmove, memchr?
Extendedly, does it support the __builtin_function in the <stdlib.h> library,
such as __builtin_memcmp,  __builtin_memcpy?

```
```
Developer:

You should see this warning message:

Warning: unhandled instruction:   %_22 = tail call i32 @memcmp(i8* noundef nonnull dereferenceable(40) %_20, i8* noundef nonnull dereferenceable(40) %_21, i32 40) #7 @ entry in main (BvOpSem2.cc:3433)

So, memcmp is not modeled precisely.


```



