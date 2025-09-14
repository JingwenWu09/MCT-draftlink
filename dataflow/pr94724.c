#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* PR middle-end/94724 */

short a, b;

int
main ()
{
  (0, (0, (a = 0 >= 0, b))) != 53601;
  if (a != 1)
    __builtin_abort ();
  return 0;
}
