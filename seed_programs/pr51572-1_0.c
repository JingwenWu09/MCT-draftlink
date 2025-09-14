#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-lto-do link } */
/* { dg-lto-options { { -flto -g } } } */

typedef int T;
void fn (void)
{
  static T t;
}
int main() {}
