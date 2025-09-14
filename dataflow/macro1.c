#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* Copyright (C) 2000 Free Software Foundation, Inc.  */

/* { dg-do run } */

/* Tests various macro abuse is correctly expanded.  */

#if DEBUG
extern int puts (const char *);
#else
#define puts(X)
#endif
extern void abort (void);
extern int strcmp(const char *s1, const char *s2);

#define j(xx, yy) xx + yy
#define k(xx, yy) j(xx + 2, yy +
#define glue(xx, yy) xx ## yy
#define xglue(xx, yy) glue(xx, yy)

/* Functions called when macros are left unexpanded.  */
int q(int x)		{return x + 40;}
int B(int x)		{return x + 20;}
int foo(int x)		{return x + 10;}
int bar(int x, int y)	{return x + y;}
int baz(int x, int y)	{return x + y;}
int toupper(int x)	{return x + 32;}
int M(int x)		{return x * 2;}

int main (int argc, char *argv[])
{
#define q(xx) xx
  if (q(q)(2) != 42){
  	do { 
  		puts("q"); 
  		abort(); 
  	} while (0);
  }

#define A(xx) B(xx)
  if (A(A(2)) != 42){
  	do { 
  		puts("A"); 
  		abort(); 
  	} while (0);
  }

#define E(xx) A xx
#define F (22)
  if (E(F) != 42){
  	do { 
  		puts("E(F)"); 
  		abort(); 
  	} while (0);
  }

#define COMMA ,
#define NASTY(a) j(a 37)
  if (NASTY (5 COMMA) != 42){
  	do { 
  		puts("NASTY"); 
  		abort(); 
  	} while (0);
  }
  
#define bar(xx, yy) foo(xx(yy, 0))
#define apply(xx, yy) foo(xx(yy, 22))
#define bam bar
  if (bar(bar, 32) != 42){
  	do { 
  		puts("bar bar"); 
  		abort(); 
  	} while (0);
  }	/* foo(bar(32, 0)).  */
  if (bar(bam, 32) != 42){
  	do { 
  		puts("bar bam"); 
  		abort(); 
  	} while (0);
  }	/* Same.  */
  if (apply(bar, baz) != 42){
  	do { 
  		puts("apply bar baz"); 
  		abort(); 
  	} while (0);
  }	/* foo(foo(baz(22, 0))).  */

  /* Taken from glibc.  */
#define __tobody(c, f) f (c)
#define toupper(c) __tobody (c, toupper)
  if (toupper (10) != 42){
  	do { 
  		puts("toupper"); 
  		abort(); 
  	} while (0);
  }	/* toupper (10). */

  /* This tests that M gets expanded the right no. of times.  Too many
     times, and we get excess "2 +"s and the wrong sum.  Derived from
     nested stpcpy in dggettext.c.  */
#define M(xx) 2 + M(xx)
#define stpcpy(a) M(a)
  if (stpcpy (stpcpy (9)) != 42){
  	do { 
  		puts("stpcpy"); 
  		abort(); 
  	} while (0);
  } /*  2 + M (2 + M (9)) */

  /* Another test derived from nested stpcpy's of dggettext.c.  Uses
     macro A(x) and function B(x) as defined above.  The problem was
     the same - excess "1 +"s and the wrong sum.  */
#define B(xx) 1 + B(xx)
#define C(xx) A(xx)
  if (C(B(0)) != 42){
  	do { 
  		puts("C"); 
  		abort(); 
  	} while (0);
  }		/* 1 + B (1 + B (0)) */

  /* More tests derived from gcc itself - the use of XEXP and COST.
     These first two should both expand to the same thing.  */
  {
    int insn = 6, i = 2, b = 2;
#define XEXP(RTX, N)  (RTX * N + 2)
#define PATTERN(INSN) XEXP(INSN, 3)
    if (XEXP (PATTERN (insn), i) != 42){
  	do { 
  		puts("XEXP (PATTERN)"); 
  		abort(); 
  	} while (0);
  }	/* ((insn * 3 + 2) * i + 2) */
    if (XEXP (XEXP (insn, 3), i) != 42){
  	do { 
  		puts("XEXP (XEXP)"); 
  		abort(); 
  	} while (0);
  }	/* ((insn * 3 + 2) * i + 2) */

#define COST(X) XEXP (XEXP (X, 4), 4)
    if (COST (b) != 42){
  	do { 
  		puts("COST"); 
  		abort(); 
  	} while (0);
  }		/* ((b * 4 + 2) * 4 + 2) */
  }

  /* This tests macro recursion and expand-after-paste.  */
#define FORTYTWO "forty"
#define TWO TWO "-two"
  if (strcmp (glue(FORTY, TWO), "forty")){
  	do { 
  		puts("glue"); 
  		abort(); 
  	} while (0);
  }
  if (strcmp (xglue(FORTY, TWO), "forty-two")){
  	do { 
  		puts("xglue"); 
  		abort(); 
  	} while (0);
  }

  /* Test ability to call macro over multiple logical lines.  */
  if (q
      (42) != 42
      || q (
	 42) != 42
      || q (42
	    ) != 42
      || q
      (
       42
       )
      != 42){
  	do { 
  		puts("q over multiple lines"); 
  		abort(); 
  	} while (0);
  }

  /* Corner case.  Test that macro expansion is turned off for later
     q, when not at start but at end of argument context, and supplied
     with the '(' necessary for expansion.  */
  if (q(1 + q)(1) != 42){
  	do { 
  		puts("Nested q"); 
  		abort(); 
  	} while (0);
  }	/* 1 + q(1) */

  /* This looks like it has too many ')', but it hasn't.  */
  if (k(1, 4) 35) != 42){
  	do { 
  		puts("k"); 
  		abort(); 
  	} while (0);
  }

  /* Phew! */
  return 0;
}
