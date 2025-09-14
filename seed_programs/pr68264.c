#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */
/* { dg-skip-if "PR68356 no math-errno on darwin" { "*-*-darwin*" } } */
/* { dg-add-options ieee } */
/* { dg-require-effective-target fenv_exceptions } */
/* { dg-skip-if "fenv" { powerpc-ibm-aix* } } */

#include <fenv.h>
#include <math.h>
#include <errno.h>

extern void abort (void) __attribute__ ((noreturn));

#define LARGE_NEG_MAYBE_ERANGE 0x01
#define LARGE_NEG_ERANGE       0x02
#define LARGE_POS_ERANGE       0x04
#define LARGE_NEG_EDOM         0x08
#define LARGE_POS_EDOM         0x10

#define LARGE_ERANGE (LARGE_NEG_ERANGE | LARGE_POS_ERANGE)
#define LARGE_EDOM (LARGE_NEG_EDOM | LARGE_POS_EDOM)
#define POWER_ERANGE (LARGE_NEG_MAYBE_ERANGE | LARGE_POS_ERANGE)


volatile double d;
volatile int i;

static void (*tester) (int);

void
check_quiet_nan (int flags __attribute__ ((unused)))
{
  if (fetestexcept (FE_ALL_EXCEPT))
     abort();
  if (errno)
     abort();
}

void
check_large_neg (int flags)
{
  if (flags & LARGE_NEG_MAYBE_ERANGE)
    return;
  int expected_errno = (flags & LARGE_NEG_ERANGE ? ERANGE
			: flags & LARGE_NEG_EDOM ? EDOM
			: 0);
  if (expected_errno != errno)
     ;
  errno = 0;
}

void
check_large_pos (int flags)
{
  int expected_errno = (flags & LARGE_POS_ERANGE ? ERANGE
			: flags & LARGE_POS_EDOM ? EDOM
			: 0);
  if (expected_errno != errno)
     ;
  errno = 0;
}

void
test (void)
{
acos (d);
tester (LARGE_EDOM);

asin (d);
tester (LARGE_EDOM);

acosh (d);
tester (LARGE_NEG_EDOM);
  
atanh (d);
tester (LARGE_EDOM);
  
cosh (d);
tester (LARGE_ERANGE);
  
sinh (d);
tester (LARGE_EDOM);
  
log (d);
tester (LARGE_NEG_EDOM);
#if defined (__sun__) && defined (__unix__)
  /* Disabled due to a bug in Solaris libm.  */
  if (0)
#endif
log2 (d);
tester (LARGE_NEG_EDOM);
log10 (d);
tester (LARGE_NEG_EDOM);
#if defined(__GLIBC__) && (__GLIBC__ < 2 || (__GLIBC__ == 2 && __GLIBC_MINOR__ < 22))
  /* Disabled due to glibc PR 6792, fixed in glibc 2.22.  */
  if (0)
#endif
log1p (d);
tester (LARGE_NEG_EDOM);
exp (d);
tester (POWER_ERANGE);
#if (defined (__sun__) || defined(__hppa__)) && defined (__unix__)
  /* Disabled due to a bug in Solaris libm.  HP PA-RISC libm doesn't support
     ERANGE for exp2.  */
  if (0)
#endif
    {
    exp2 (d);
    tester (POWER_ERANGE);
#if defined(__GLIBC__) && (__GLIBC__ < 2 || (__GLIBC__ == 2 && __GLIBC_MINOR__ < 11))
      /* Disabled due to glibc PR 6788, fixed in glibc 2.11.  */
      if (0)
#endif
expm1 (d);
tester (POWER_ERANGE);
    }
sqrt (d);
tester (LARGE_NEG_EDOM);
pow (100.0, d);
tester (POWER_ERANGE);
pow (i, d);
tester (POWER_ERANGE);

}

int
main (void)
{
  errno = 0;
  i = 100;
  d = __builtin_nan ("");
  tester = check_quiet_nan;
  feclearexcept (FE_ALL_EXCEPT);
  test ();

  d = -1.0e80;
  tester = check_large_neg;
  errno = 0;
  test ();

  d = 1.0e80;
  tester = check_large_pos;
  errno = 0;
  test ();

  return 0;
}
