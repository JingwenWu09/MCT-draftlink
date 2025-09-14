# Bug #10 in CBMC was confirmed as a language semantics related issue. It was exposed by a test case generated using common subexpression elimination transformation.


```
Me:

#include <assert.h>
#include <math.h>
int main() {
  int i;
  unsigned int A[99];
  float z = 158901.81;
  short u = 10607;
  int iA = 0;
  double D[99];
  char w = 0;
  unsigned long v = 1703572782;
  int B[99];
  unsigned char x = 87;
  float g = 29.14;
  int iB = 0;
  unsigned int A_1[99];
  int iA_1 = 0;
  int B_1[99];
  
  int i_1 = i;
  for (i = 1; i < 100; i++) {
  	A[iA] = z * iA - u;
  	D[iA] = A[iA] * w - v;
  	iA++;
    ;
  }
  for( ; iB < iA; iB++ ) {
  	B[iB] = D[iB] * x - g;
  }
  
  for (i_1 = 1; i_1 < 100; i_1++) {
  	A_1[iA_1] = z * iA_1 - u;
  	B_1[iA_1] = (double)(A_1[iA_1] *w - v) * x - g;
  	iA_1++;
    ;
  }

  assert(i == i_1) ;
  for(int i = 0; i < 99; i++) {
  	assert(A[i] == A_1[i]) ;
  }
  for(int i = 0; i < 99; i++) {
  	assert(B[i] == B_1[i]) ;//failed
  }
  return 0;
}

CBMC gives the FAILURE result towards assert(B[i] == B_1[i]) which means 
this equivalent transformation in the example is incorrect. 

CBMC version: 5.88.0
Operating system: Ubuntu 22.04, MacOS
command line: cbmc example.c

```
