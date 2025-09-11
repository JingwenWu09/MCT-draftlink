# Bug #2 in CPAchecker was confirmed as a C standard library related issue. It was exposed by a test case generated using dead code elimination transformation. 
```
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

main() {
  char *s = ":ab";
	char *s = arr;
  char *lim = s + 3;

  int tab = ':';
  char *ptr = s;
  int sword = 1;
  int schar = 1;

  char arr_1[3] = ":ab";
  char *s_1 = arr_1;
  char * ptr_1 =  s_1;
  char * lim_1 =  s_1 + 3;
  int sword_1 = sword;
  int tab_1 = tab;
  
  if (tab) {
    while (ptr < lim && sword--) {
      while (ptr < lim && *ptr != tab) {
        ++ptr;
      }
      if (ptr < lim) {
        ++ptr;
      }
    }
  } else {
    while (1) {
      ;
    }
  }

  //after DCE
  while (ptr_1 < lim_1 && sword_1--) {
    while (ptr_1 < lim_1 && *ptr_1 != tab_1) {
      ++ptr_1;
    }
    if (ptr_1 < lim_1) {
      ++ptr_1;
    }
  }
  assert(sword == sword_1);

	assert(ptr<lim);
  if (ptr + schar <= lim) {
    ptr += schar;
  }

  if (ptr != s + 2) {
    abort();
  }
  exit(0);
}

```
```
Me:

Example 1:
int main() {
  char *a = ":ab";
  char *a_1 = ":ab";
  int cnt = 0;
  for (; *a != 0; a++) {
      cnt++;
  }
  
  int cnt_1 = 0;
  for (; *a_1 != 0; a_1++) {
      cnt_1++;
  }
 	
  assert(cnt == cnt_1);
  return 0;
}

Example2:
int main() {
  char arr[6] = {':','a','b'};
  char *a = arr;

  char arr_1[6] = {':','a','b'};
  char *a_1 = arr_1;

  int cnt = 0;
  for (; *a != 0; a++) {
    cnt++;
  }
  
  int cnt_1 = 0;
  for (; *a_1 != 0; a_1++) {
    cnt_1++;
  }
 	
  assert(cnt == cnt_1);
  return 0;
}

For Example1.c, the assert() function is true, which is confirmed by compiling with gcc and clang,
while CPAchecker gave the UNKNOWN verification result.

But after we transformed Example1.c into Example2.c, CPAchecker gave the TRUE verification result.
Is it related that the pointer variable 'a' points to a string "hello"?

command:
/scripts/cpa.sh -kInduction -preprocess -64 -setprop cpa.predicate.ignoreIrrelevantVariables=false filename.c 

```
```
Developer:

In this case, the missing proof is because by default we ignore string initializers,
because in most programs these are only for log messages etc., but not related to the property.
You can enable handling them with -setprop cpa.predicate.handleStringLiteralInitializers=true.
Then CPAchecker produces TRUE for both programs.
Note that the fact that in Example1.c there is a string and in Example2.c there is a char array is not relevant.
CPAchecker would produce the same behavior even if you make Example2.c use a string by adding a null byte.
```

