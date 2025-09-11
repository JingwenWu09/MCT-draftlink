# Bug #20 in CPAchecker was confirmed as a memory allocation related issue. It was exposed by a test case generated using dead code elimination transformation. 

```
Me:
//Original Mutate (not reduced)
#include <assert.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a[6], b, c = 226, d, e, f;
int *d_global_p = &d; //globalVarAnnotation
int *b_global_p = &b; //globalVarAnnotation
int *e_global_p = &e; //globalVarAnnotation
int *f_global_p = &f; //globalVarAnnotation
int *a_global_p = &a[0]; //globalVarAnnotation
int *c_global_p = &c; //globalVarAnnotation
signed char g;
signed char *g_global_p = &g; //globalVarAnnotation

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
  //renameUseVarStmt
  //addVarStmt
  char addVar = 18;
  char addVar_1 = addVar;
  //initial_block
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


```
```
Me:
//The simplest form that triggers this bug:

Example 1:
int arr[2];
void fun(int p){
	arr[p] = 2;		//smg false
}
int main(){
	int n = 2;
	fun( n >> 2 & 1  );
	return 0;	
}

Example 2:
int arr[2];
void fun(int p){
	arr[p] = 1;
}
int main(){
	int n = 2;
	int p = n >> 2 & 1;
	fun( p );
	return 0;	
}

Example 3:
int arr[2];
int main(){
	int n = 2;
	int p = n >> 2 & 1;
	arr[p] = 1;
	return 0;	
}

Example 4:
int arr[2];
void fun(int p){
	assert(p == 0);
	arr[p] = 2;
}
int main(){
	int n = 2;
	fun( n >> 2 & 1  );
	return 0;	
}

In Example 1, the SMGAnalysis gives False verification result with the following message,
showing that the statement arr[p] = 1; has invalid pointer dereference :
Verification result: FALSE. Property violation (valid-deref: invalid pointer dereference in line4) found by chosen configuration.

However, after I equivalently transformed Example 1 into Example 2 and Example 3, the SMGAnalysis gave both True results.

In addition, I added the assertion statement assert( p == 0 ); in Example 1 as shown in Example 4.
Then the PredicateAnalysis, ValueAnalysis and KInduction all gave TRUE results that means the assertion was true,
while the SMGAnalysis result became UNKNOWN with the following message, which showed that the assertion was failed:
Error: Unknown function '__assert_fail' may be unsafe. See the cpa.smg.handleUnknownFunctions or cpa.smg.safeUnknownFunctionsPatterns
(SMGBuiltins.handleUnknownFunction, SEVERE)

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
