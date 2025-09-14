#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort();

union u {
  int a;
} __attribute__((__ms_struct__, __packed__));

struct s {
  char c;
  union u u;
};

int main(void) {
  if (sizeof(struct s) != (sizeof(char) + sizeof(union u))) {
    abort();
  }

  return 0;
}
