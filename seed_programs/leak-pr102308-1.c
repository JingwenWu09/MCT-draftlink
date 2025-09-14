#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct s {
  char *p;
  int arr[2];
};

int main(void) {
  struct s *s = malloc(sizeof *s);
  if (s) {
    s->p = malloc(1);
    for (int i = 0; i < 2; i++) {
      s->arr[i] = -1;
    }
  }
  if (s) {
    free(s->p);
    free(s);
  }
}
