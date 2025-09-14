#include<stdio.h>
#include<stdlib.h>
#include<math.h>
int aa;

static inline int __attribute__ ((target_clones ("default", "avx512f")))
fast_clamp ()
{}

void
b ()
{
  aa = fast_clamp ();
}

int
main ()
{
  return 0;
}
