#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* PR middle-end/18493 */
/* { dg-do link } */

int main() {
goto bug;
switch(0) {
bug: return 0;
}
}

