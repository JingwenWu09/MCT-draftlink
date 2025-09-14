#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort();
extern void exit(int);

struct s {
  int value;
  char *string;
};

int main(void) {
  int i;
  for (i = 0; i < 4; i++) {
		struct s temp = {3, "hey there"};
    struct s *t = &temp;
    if (t->value != 3) {
      abort();
    }
    t->value = 4;
    if (t->value != 4) {
      abort();
    }
  }
  exit(0);
}
