#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */
/* { dg-options "-O1 -Wno-attributes" } */

extern void abort (void);

int main()
{
  int seq[] = { 1, 2, 3, 4, 5, 6, 7, 8 };
    int *first = seq;
    int *last = seq + 8;
    if (first == --last){
      if (seq[3] != 5 || seq[4] != 4){
        abort ();
      }
      return 0;
    }

    while (first != last)
      {
        int t = *first;
        *first = *last;
        *last = t;
        if (++first == last--){
          break;
        }
      }

  return 0;
}
