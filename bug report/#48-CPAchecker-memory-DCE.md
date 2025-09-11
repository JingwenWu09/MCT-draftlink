# Bug #20 in CPAchecker was confirmed as a memory allocation related issue. It was exposed by a test case generated using dead code elimination transformation.
```
//Original Mutate (not reduced)
#include <assert.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>
#include <string.h>

#define str(t) #t

int main(void) {
  const char s[] = str(\U000030b2);

//storeGlobalVarStmt
  //renameUseVarStmt
  const char  s_1[4];
  char *s_p = &s[0];
  char *s_1_p = &s_1[0];
  for(int i = 0; i < sizeof(s)/sizeof(s[0]); i++) {
  	*(s_1_p+i) = *(s_p+i);
  }
  //initial_block
  if (strcmp(s, "ゲ") != 0) {
    abort();
  }
//restoreGlobalVarStmt
  //trans_block_false
  strcmp(s_1, "ゲ") != 0;
  //conditionStmt
  char *s_cmp = &s[0];
  char *s_1_cmp = &s_1[0];
  for(int i = 0; i < sizeof(s)/sizeof(s[0]); i++) {
  	assert(s_cmp[i] == s_1_cmp[i]) ;
  }

  return 0;
}

```
```
Me:

//The simplest form that triggers this bug:
int main(){
	const char s[] = "\U000030b2";		//smg : false
	char s[] = "\U000030b2";		//smg : true
	const char s[] = "\U0000";		//smg : true
	return 0;
}

In this case, there are three statements in the main function that assign a char array of a string.
Following the first statement, the second statement removes the const modifier,
and the third statement changes the value of the string.
Cpachecker's SMGAnalysis of these three statements gives the verification results of FALSE, TRUE and TRUE respectively.
Why are the SMGAnalysis's results different for the three statements assigning a string to a char array,
and is it related to the modifiers(e.g.,const) and the value of the string?

command line:
./scripts/cpa.sh -smg -spec ./config/properties/valid-memsafety.prp -preprocess -64 -setprop
cpa.predicate.ignoreIrrelevantVariables=false -setprop cpa.predicate.handleStringLiteralInitializers=true <filename.c>


```
```
Developer:

SMGAnalysis for assigning a string to a char array.
This can be solved by using the new SMG2 analysis.

```
