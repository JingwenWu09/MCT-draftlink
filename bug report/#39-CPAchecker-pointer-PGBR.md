# Bug #12 in CPAchecker was fixed as a pointer alias related issue. It was exposed by a test case generated using probability-guided branch reordering  transformation. 

```
Me:

#include<assert.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  int one = 1;

  if (sizeof(int) != 4 * sizeof(char)) {
    exit(0);
  }

  for (int i = 0; i < 2; i++) {
    char *bp = (char *)&one;
    unsigned n = sizeof(one);
    register char c;
    register char *ep = bp + n;
    register char *sp;

//storeGlobalVarStmt
    //renameUseVarStmt
    int one_1 = one;
    char * bp_1 =  (char *)&one_1;
    char * ep_1 =  bp_1 + n;
    char * sp_1;
    char c_1 = c;
    while (bp < ep) {
      sp = bp + 3;
      c = *sp;
      *sp = *bp;
      *bp++ = c;
      sp = bp + 1;
      c = *sp;
      *sp = *bp;
      *bp++ = c;
      bp += 2;
    }
//restoreGlobalVarStmt
if (bp_1 < ep_1){
    do {
      sp_1 = bp_1 + 3;
      c_1 = *sp_1;
      *sp_1 = *bp_1;
      *bp_1++ = c_1;
      sp_1 = bp_1 + 1;
      c_1 = *sp_1;
      *sp_1 = *bp_1;
      *bp_1++ = c_1;
      bp_1 += 2;
    }while (bp_1 < ep_1);
}
    //conditionStmt
    assert(one == one_1) ;
    assert(c == c_1) ;
  }

  if (one != 1) {
    abort();
  }

  exit(0);
}


In this case, the assert() functions are both true, which is confirmed by compiling with gcc and clang,
while CPAchecker gives the FALSE result which means the assertion is false.
Command: scripts/cpa.sh -predicateAnalysis -64 -preprocess -setprop
cpa.predicate.ignoreIrrelevantVariables=false <file_name.c>
```
```
Developer:

The C code is a little bit hard to follow. But I notice that it casts from int* to char*.
Does it write into that char pointer then?
If yes, the program requires on correct aliasing for char* and int*,
which is currently not implemented in the predicate analysis of CPAchecker. This is tracked by #296.
There is an experimental option cpa.predicate.useByteArrayForHeap=true that is supposed to help in such cases,
but it is not yet fully implemented. But it does turn the wrong result into a solver failure.

```

```
Developer:

One idea how to make this at least sound is that whenever a write to e.g. an int pointer is done,
we simulate another write of non-deterministic bytes through a char pointer.
And if a write through a char pointer is done, we similarly set the respective memory cells for all other types to non-deterministic.
This is a little bit expensive because when not using quantifiers or arrays for the heap,
a pointer write can mean creating a large formula,
but maybe the cost is acceptable (we should at least have such an option).

```

