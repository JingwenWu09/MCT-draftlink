#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>

void test_1(void *ptr) {
  free(ptr);
  free(ptr);
}

struct s {
  void *ptr;
};

void test_2(struct s *x) {
  free(x->ptr);
  free(x->ptr);
}

int main() {
}
