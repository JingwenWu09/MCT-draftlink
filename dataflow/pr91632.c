#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* PR tree-optimization/91632 */
/* { dg-additional-options "-fwrapv" } */

static int
__attribute__((noipa))
foo (char x)
{
  switch (x)
    {
    case '"':
    case '<':
    case '>':
    case '\\':
    case '^':
    case '`':
    case '{':
    case '|':
    case '}':
      return 0;
    }
  return 1;
}

int
main ()
{
  if (foo ('h') == 0)
    __builtin_abort ();
  return 0;
}
