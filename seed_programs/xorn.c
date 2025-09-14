#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int xorn(int a, int b){ 
  return a ^ ~b; 
}

int not(int a){
  return ~a; 
 }

int xor (int a, int b){
  return a ^ b; 
 }

main() {
  int i, j;

  for (i = 0; i <= 1; i++) {
    for (j = 0; j <= 1; j++) {
      printf("%d op %d = %d = %d?\n", i, j, 1 & xor(i, not(j)), 1 & xorn(i, j));
    }
  }
}
