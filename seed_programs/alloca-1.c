#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-skip-if "requires alloca" { ! alloca } { "-O0" } { "" } } */
/* Verify that alloca storage is sufficiently aligned.  */
/* ??? May fail if BIGGEST_ALIGNMENT > STACK_BOUNDARY.  Which, I guess
   can only happen on !STRICT_ALIGNMENT targets.  */

typedef __SIZE_TYPE__ size_t;

struct dummy { int x __attribute__((aligned)); };

_Bool foo(void)
{
  char *p = __builtin_alloca(32);
  return ((size_t)p & (__alignof__(struct dummy) - 1)) == 0;
}

int main()
{
  if (!foo())
    abort ();
  return 0;
}
