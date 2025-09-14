# Bug #16 in CPAchecker was fixed as a memory allocation related issue. It was exposed by a test case generated using operator strength reduction transformation. 

```
Me:

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  char *p;
  p = "abc\n";

  char * p_1 =  "abc\n";
  unsigned int left_RQPw[3];
  short c_QtRB = 3317;
  unsigned short d_hPAu = 63962;
  int i_JxWw = 0;
  int randomNum1 = 54;
  int iiii1 = 0;
  float left_fhd[3];
  int c_bPAI = -28398051;
  unsigned int d_JBps = -1861615876;
  int i_YhhN = 18 * 3;
  int randomNum2 = 18;
  int iiii2 = 0;
  while( !(*p == '\n' || *p == ';' || *p == '!') && i_JxWw < randomNum1 * 3 && i_YhhN > 0 ){
    left_RQPw[iiii1] = c_QtRB * i_JxWw - d_hPAu;
    left_fhd[iiii2] = c_bPAI * i_YhhN - d_JBps;
    i_JxWw += randomNum1;
    i_YhhN -= randomNum2; 
    iiii1++;
    iiii2++;
    p++;
  }

  unsigned int left_RQPw1[3];
  float left_fhd1[3];
  int iiii1_1 = 0;
  int iiii2_1 = 0;
  int init0 = 0;
  int s_OAgC = init0 - d_hPAu;
  unsigned int s_Ycbe = c_bPAI * (randomNum2 * 3) - d_JBps;
  while( !(*p_1 == '\n' || *p_1 == ';' || *p_1 == '!') && s_OAgC != c_QtRB * (randomNum1 * 3) - d_hPAu && s_Ycbe !=  - d_JBps ){
    left_RQPw1[iiii1_1] = s_OAgC;
    left_fhd1[iiii2_1] = s_Ycbe;
    s_OAgC = s_OAgC + c_QtRB * randomNum1;
    s_Ycbe = s_Ycbe - c_bPAI * randomNum2;
    iiii1_1++;
    iiii2_1++;
    p_1++;
  }
  
  for(int i=0; i<3; i++){
    assert(left_RQPw[i] == left_RQPw1[i]);
  }
  for(int i=0; i<3; i++){
    assert( left_fhd[i] == left_fhd1[i] );
  }
  return 0;
}

In this case, SMGAnalysis on the latest source code and release version gave different results
(source code was FALSE and release was TRUE). Here is the Counterexample.1.core.txt of source code.

N4 -{while}-> N5
N5 -{[*p == '\n']}-> N6

I combined the declaration char *p; and the assignment p = "abc";  to form statement char *p = "abc", to get another one.
Then SMGAnalysis in both methods gave TRUE results.

In this case, which reslut is correct and what has been fixed in the wrong one?
Why does the SMGAnalysis of the source code give different results for the different assignment method,
is it related to the way the pointer p is assigned?

command line:
./scripts/cpa.sh -smg -spec ./config/properties/valid-memsafety.prp -preprocess -64 <filename.c>

```
```
Developer:

This is a bug in both SMG and SMG2. Thank you for reporting this!
I will take a look and fix it soon. I'll report back here.

```
```
Developer:

This has been fixed for SMG2.
```

