#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* Copyright (C) 2000 Free Software Foundation, Inc.  */

/* { dg-do run } */
/* { dg-options "-std=c99 -pedantic-errors" } */

/* Test ## behavior and corner cases thoroughly.  The macro expander
   failed many of these during development.  */

#ifndef __WCHAR_TYPE__
#define __WCHAR_TYPE__ int
#endif
typedef __WCHAR_TYPE__ wchar_t;

extern int strcmp (const char *, const char *);
#if DEBUG
extern int puts (const char *);
#else
#define puts(XX)
#endif
extern void abort (void);

#define EMPTY
#define str(xx) #xx
#define xstr(xx) str(xx)
#define glue(xx, yy) xx ## yy
#define xglue(xx, yy) glue (xx, yy)
#define glue3(xx, yy, zz) xx ## yy ## zz
#define glue_var(xx, ...) xx ## __VA_ARGS__

#define __muldi3 __NDW(mul, 3 = 50)
#define __NDW(aa,bb) __ ## aa ## di ## bb
#define m3 NDW()
#define NDW(xx) m3 ## xx = 50
#define five 5
#define fifty int fif ## ty

/* Defines a function called glue, returning what it is passed.  */
int glue (glue,) (int x)
{
  return x;
}

int main ()
{
  /* m3 and __muldi3 would sometimes cause an infinite loop.  Ensure
     we only expand fifty once.  */
  fifty = 50, m3, __muldi3;

  /* General glue and macro expanding test.  */
  int five0 = xglue (glue (fi, ve), 0);

  /* Tests only first and last tokens are pasted, and pasting to form
     the != operator.  Should expand to: if (five0 != 50).  */
  if (glue3 (fi, ve0 !,= glue (EMPTY 5, 0))){
  	do { 
  		puts("five0 != 50"); 
  		abort(); 
  	} while (0);
  }

  /* Test varags pasting, and pasting to form the >> operator.  */
  if (glue_var(50 >, > 1 != 25)){
  	do { 
  		puts("Operator >> pasting"); 
  		abort(); 
  	} while (0);
  }

  /* The LHS should not attempt to expand twice, and thus becomes a
     call to the function glue.  */
  if (glue (gl, ue) (12) != 12){
  	do { 
  		puts("Recursive macros"); 
  		abort(); 
  	} while (0);
  }

  /* Test placemarker pasting.  The glued lines should all appear
     neatly in the same column and below each other, though we don't
     test that here.  */
  {
    int glue3(a, b, ) = 1, glue3(a,,) = 1;
    glue3(a, , b)++;
    glue3(, a, b)++;
    glue3(,a,)++;
    glue3(,,a)++;
    if (a != 3 || ab != 3 glue3(,,)){
  	do { 
  		puts("Placemarker pasting"); 
  		abort(); 
  	} while (0);
  }
  }

  /* Test that macros in arguments are not expanded.  */
  {
    int glue (EMPTY,1) = 123, glue (T, EMPTY) = 123;
    if (EMPTY1 != 123 || TEMPTY != 123){
  	do { 
  		puts("Pasted arguments macro expanding"); 
  		abort(); 
  	} while (0);
  }
  }

  /* Test various paste combinations.  */
  {
    const wchar_t* wc_array = glue(L, "wide string");
    wchar_t wc = glue(L, 'w');
    const char * hh = xstr(xglue(glue(%, :), glue(%, :)));
    int array glue (<, :) 1 glue (:, >) = glue(<, %) 1 glue(%, >);
    int x = 4;

    if (array[0] != 1){
  	do { 
  		puts("Digraph pasting"); 
  		abort(); 
  	} while (0);
  }

    x glue (>>, =) 1;		/* 2 */
    x glue (<<, =) 1;		/* 4 */
    x glue (*, =) 2;		/* 8 */
    x glue (+, =) 100;		/* 108 */
    x glue (-, =) 50;		/* 58 */
    x glue (/, =) 2;		/* 29 */
    x glue (%, =) 20;		/* 9 */
    x glue (&, =) 254;		/* 8 */
    x glue (|, =) 16;		/* 24 */
    x glue (^, =) 18;		/* 10 */
    
    if (x != 10 || 0 glue (>, =) 1 glue (|, |) 1 glue (<, =) 0){
  	do { 
  		puts("Various operator pasting"); 
  		abort(); 
  	} while (0);
  }

    if (strcmp (hh, "%:%:")){
  	do { 
  		puts("Pasted digraph spelling"); 
  		abort(); 
  	} while (0);
  }

    if ((glue (., 1) glue (!, =) .1)){
  	do { 
  		puts("Pasted numbers 1"); 
  		abort(); 
  	} while (0);
  }
 
    /* glue3 here will only work if we paste left-to-right.  If a
       future implementation does not do this, change the test.  */
    if (glue3 (1.0e, +, 1) != 10.0){
  	do { 
  		puts("Pasted numbers 2"); 
  		abort(); 
  	} while (0);
  }
  }

  return 0;
}
