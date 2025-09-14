#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void mem_init(void);
int ARCHnodes, ARCHnodes1;
int ***vel;

int main(int argc, char **argv) {
  int i, j, k;

  ARCHnodes = 2;
  ARCHnodes1 = 4;

  mem_init();

  for (i = 0; i < ARCHnodes; i++) {
    for (j = 0; j < 3; j++) {
      for (k = 0; k < ARCHnodes1; k++) {
        printf("[%d][%d][%d]=%d ", i, j, k, vel[i][j][k]);
      }
      printf("\n");
    }
    printf("\n");
  }
  for (i = 0; i < ARCHnodes; i++) {
    for (j = 0; j < 3; j++) {
      printf("%x\n", vel[i][j]);
    }
  }

  for (i = 0; i < ARCHnodes; i++) {
    free(vel[i]);
  }

  free(vel);
  return 0;
}

void mem_init(void) {

  int i, j, k, d;

  d = 0;
  vel = (int ***)malloc(ARCHnodes * sizeof(int **));

  for (i = 0; i < ARCHnodes; i++) {
    vel[i] = (int **)malloc(3 * sizeof(int *));
    if (vel[i] == (int **)NULL) {
      printf("malloc failed for vel[%d]\n", i);
      exit(0);
    }
  }
  for (i = 0; i < ARCHnodes; i++) {
    for (j = 0; j < 3; j++) {
      vel[i][j] = (int *)malloc(ARCHnodes1 * sizeof(int));
      printf("%x %d %lu\n", vel[i][j], ARCHnodes1, sizeof(int));
    }
  }
  for (i = 0; i < ARCHnodes; i++) {
    for (j = 0; j < 3; j++) {
      printf("%x\n", vel[i][j]);
    }
  }

  printf("again:\n\n");
  for (i = 0; i < ARCHnodes; i++) {
    for (j = 0; j < 3; j++) {
      printf("%x\n", vel[i][j]);
    }
  }
}
