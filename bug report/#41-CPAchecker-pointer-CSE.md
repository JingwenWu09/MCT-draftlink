# Bug #14 in CPAchecker was confirmed as a pointer alias related issue. It was exposed by a test case generated using common subexpression elimination transformation. 
```
Me:

unsigned char c[256] = {0xdc}, c1[256], y[256] = {0xcc}, y1[256];
void main() {
  int i, h = 0, h1 = h;
  unsigned char j = 0, j1 = j;
  unsigned char *py = &y[0], *pc = &c[0];
  for ( i = 0; i < 256; i++ ) {
    y1[i] = py[i]; c1[i] = pc[i];
  }
  for ( i = 0; i < 5; ++i ) {
    j = y[i] ^ c[i];
    y[i] = c[j] ^ h + y[i] ^ c[i];
    h = y[i];
  }
  for (i = 0; i < 5; ++i) {
    int t = y1[i] ^ c1[i]; j1 = t;
    y1[i] = c1[j1] ^ h1 + t;
    h1 = y1[i];
  }
  assert (h == h1); 
}

In this case, CPAchecker predicate analysis gives the FALSE verification result.
I used delta debugging in the source code, finding that it may have some questions
in the ValueAnalysisTransferRelation.java and PointerTransferRelation.java.
Please help me to explain this situation.

```
```
Developer:

Predicate analysis unsound when writing through "irrelevant" variable that is aliased with relevant variable.

The reason for this is a heuristic in our predicate analysis that treats variables as irrelevant
and there ap is detected as irrelevant because it is never used again.
We fail to take the aliasing with a into account. Thus the writes to ap are ignored.
This heuristic can be turned off with -setprop cpa.predicate.ignoreIrrelevantVariables=false,
and then the result is correct.

```

