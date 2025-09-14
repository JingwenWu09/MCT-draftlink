#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* PR middle-end/38533 */
/* { dg-do compile } */
/* { dg-options "-O2 --param tree-reassoc-width=1 -fdump-tree-reassoc1" } */


int
foo (void)
{
  int e = 0, f;
  for(int i=0; i<2; i++){
  	for(int j=0;j<11; j++){
  		for(int k=0; k<11;k++){
  			asm volatile ("" : "=r" (f) : "0" (0)); 
  			e |= f;
  		}
  	}
  }
  
  for(int i=0; i<5; i++){
  	for(int j=0;j<11; j++){
		asm volatile ("" : "=r" (f) : "0" (0)); 
		e |= f;
  	}
  }
  
  for(int i=0;i<6; i++){
		asm volatile ("" : "=r" (f) : "0" (0)); 
		e |= f;
  	}
  
  return e;
}

int
main (void)
{
  if (foo ())
    __builtin_abort ();
  return 0;
}

/* Verify that reassoc hasn't increased register pressure too much
   by moving all bitwise ors after the last __asm__.  There should
   be exactly 2 (first) __asm__ stmts with no intervening stmts,
   all others should have some bitwise or in between.  */
/* { dg-final { scan-tree-dump-times "__asm__\[^;\n]*;\n *__asm__" 1 "reassoc1"} } */
