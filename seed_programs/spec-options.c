#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* Check that -mfoo is accepted if defined in a user spec
   and that it is not passed on the command line.  */
/* Must be processed in EXTRA_SPECS to run.  */
/* { dg-do run } */
/* { dg-options "-B${srcdir}/gcc.dg --specs=foo.specs -tfoo" } */

extern void abort(void);

int main(void)
{
#ifdef FOO
  abort();
#else
  return 0;
#endif
}
