#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* Declaration of the frame size doesn't work on ptx.  */
/* { dg-require-effective-target untyped_assembly } */
/* { dg-require-effective-target indirect_calls } */

#include <limits.h>
# define ASIZE 4096

extern void abort (void);

int __attribute__((noinline))
bar (void)
{
  char s[ASIZE];
  s[0] = 'a';
  s[ASIZE - 1] = 'b';
    char *sp = s;
    if (!sp)
        goto next1;
    if (sp[0] != 'a')
      abort ();
    sp += ASIZE - 1;
    if (sp[0] != 'b')
      abort ();
next1:
    sp = s;
    if (!sp)
      goto next2;
    if (sp[0] != 'a')
      abort ();
    sp += ASIZE - 1;
    if (sp[0] != 'b')
      abort ();
next2:
  return 0;
}

int __attribute__((noinline))
baz (long i)
{
  if (i)
    return 1;
  else
    {
      char s[ASIZE];
      s[0] = 'a';
      s[ASIZE - 1] = 'b';
        char *sp = s;
        if (!sp)
            goto next1;
        if (sp[0] != 'a')
          abort ();
        sp += ASIZE - 1;
        if (sp[0] != 'b')
          abort ();
    next1:
        sp = s;
        if (!sp)
          goto next2;
        if (sp[0] != 'a')
          abort ();
        sp += ASIZE - 1;
        if (sp[0] != 'b')
          abort ();
    next2:
      return 1;
    }
}

int
main (void)
{
  if (bar ()){
    abort ();
  }
  if (baz (0) != 1){
    abort ();
  }
  if (baz (1) != 1){
    abort ();
  }
  return 0;
}
