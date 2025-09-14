#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* PR tree-optimization/98182 */
/* { dg-do compile } */
/* { dg-options "-O1 --param case-values-threshold=1 -fdump-tree-iftoswitch-optimized" } */

int global;
int foo ();

int main(int argc, char **argv)
{
  if (argc != 1)
    ;
  else if (argc != 2)
    ;
  else
    return 0;
}

/* { dg-final { scan-tree-dump-not "Condition chain" "iftoswitch" } } */
