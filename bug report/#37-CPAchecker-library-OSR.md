# Bug #10 in CPAchecker was fixed as a C standard library related issue. It was exposed by a test case generated using operator strength reduction transformation.

```
Me:

int main() {
  int a = 5; 
  double b = 2.0;
  double c1 = pow(b, a); 
  int c2 = (1 << a);
  assert(fabs(c1 - (double)c2) < 0.000001);
  assert(c2 == 32);
  assert(fabs(c1 - 32.0) < 0.000001);
  return 0;
}

In this case, the assert() function is true, which is confirmed by compiling with gcc and clang,
while CPAchecker gives the FALSE result which means the assertion is false.

Command: ~/cpachecker/scripts/cpa.sh -predicateAnalysis -64 -preprocess
-setprop cpa.predicate.ignoreIrrelevantVariables=false <file_name.c>

```
```
Developer:

Linked to Support more floating point functions from C standard in predicate analysis.
The functions pow and fabs are currently not implemented in the predicate analysis.
There is an existing issue for the general topic of float-related functions,
so we'll keep track of this under that other issue.

```

