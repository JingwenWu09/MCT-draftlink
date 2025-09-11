# Bug #3 in CPAchecker was confirmed as a lifetime model related issue. It was exposed by a test case generated using constant propagation transformation.
```
Me:

#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int g(unsigned long long int *v, int n, unsigned int a[], int b) {
  int cnt;
  *v = 0;
  for (cnt = 0; cnt < n; ++cnt) {
    *v = *v * b + a[cnt];
  }
  return n;
}

main() {
  int res;
  unsigned int ar[] = {10, 11, 12, 13, 14};
  unsigned long long int v;
  int t;

  int res1 = res;
  unsigned long long int v = v1;
  res = g(&v, sizeof(ar) / sizeof(ar[0]), ar, t);
  res1 = g(&v, 5, ar, t);

  assert(res == res1);
  exit(0);
}


In Example.c, CPAchecker gave both TRUE results in kInduction and predicateAnalysis
while gave UNKNOWN result in valueAnalysis.
Please help me to explain why the result of valueAnalysis is different form the others.
Is it related that the variable 't' is uninitialized?

command:
scripts/cpa.sh -kInduction -64 -preprocess -setprop cpa.predicate.ignoreIrrelevantVariables=false <file_name.c>
scripts/cpa.sh -predicateAnalysis -64 -preprocess -setprop cpa.predicate.ignoreIrrelevantVariables=false <file_name.c>
scripts/cpa.sh -valueAnalysis -64 -preprocess -setprop cpa.predicate.ignoreIrrelevantVariables=false <file_name.c>

```
```
Developer:

An explicit-value domain stores explicit values for variables, not constraints,
and it does not keep track of relations between values. 
```

