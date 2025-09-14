# Bug #20 in CPAchecker was confirmed as a memory allocation related issue. It was exposed by a test case generated using dead code elimination transformation. 

```
Me:

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

int a[6], b, c = 226, d, e, f;
int *d_global_p = &d; 
int *b_global_p = &b; 
int *e_global_p = &e; 
int *f_global_p = &f; 
int *a_global_p = &a[0]; 
int *c_global_p = &c; 
signed char g;
signed char *g_global_p = &g; 

void fn1(int p1) {
  b = a[p1];
}

int main() {
  a[0] = 1;
  for (f = 0; f < 9; f++) {
    signed char h = c;
    int i = 1;
    g = h < 0 ? h : h >> i;
    e = g;
    for (d = 1; d; d = 0) {
      ;
    }
  }
  fn1(g >> 8 & 1);

  //storeGlobalVarStmt
  int d_store = *d_global_p;
  int b_store = *b_global_p;
  int e_store = *e_global_p;
  int f_store = *f_global_p;
  int a_store[6];
  int *a_store_p = &a_store[0];
  for(int i = 0; i < sizeof(a_store)/sizeof(a_store[0]); i++) {
    *(a_store_p+i) = *(a_global_p+i);
  }
  int c_store = *c_global_p;
  signed char g_store = *g_global_p;
  char addVar = 18;
  char addVar_1 = addVar;
  if (b != 0) {
    __builtin_abort();
  }
  //restoreGlobalVarStmt
  *d_global_p = d_store;
  *b_global_p = b_store;
  *e_global_p = e_store;
  *f_global_p = f_store;
  for(int i = 0; i < sizeof(a_store)/sizeof(a_store[0]); i++) {
	*(a_global_p+i) = *(a_store_p+i);
  }
  *c_global_p = c_store;
  *g_global_p = g_store;
  //trans_block_false
  b != 0;
  //conditionStmt
  assert(addVar == addVar_1) ;

  return 0;
}

In this program, SMGAnalysis reports a false memory-safety violation (invalid pointer dereference),
while other analyses (PredicateAnalysis, ValueAnalysis, KInduction) verify the assertions successfully.
We also observed that when logically equivalent rewritings are applied to expressions inside the program
(e.g., factoring out the argument to fn1 into a separate variable before the call), SMGAnalysis produces TRUE instead of FALSE.

This inconsistency suggests that the result is sensitive to the particular syntactic form of pointer assignments,
rather than the underlying semantics, which is confirmed by running the same program under GCC and Clang with no violations.

command line:
./scripts/cpa.sh -valueAnalysis -preprocess -64 -setprop cpa.predicate.ignoreIrrelevantVariables=false -setprop
cpa.predicate.handleStringLiteralInitializers=true <filename.c>

./scripts/cpa.sh -smg -spec ./config/properties/valid-memsafety.prp -preprocess -64 -setprop
cpa.predicate.ignoreIrrelevantVariables=false -setprop cpa.predicate.handleStringLiteralInitializers=true <filename.c>

```

```
Developer:

Unfortunately the SMGAnalysis has some problems and will be replaced by a new SMG analysis soon.
You can try the new analysis with -smg2. But please keep in mind that the
new analysis is currently a explicit value analysis only (while the old SMG analysis is symbolic execution),
meaning that they do different things.
For best results i would recommend using both on a task with the order of SMG2 first, then SMG.
I tested your initial problem with SMG2 by the way, and we return TRUE for that.

```
