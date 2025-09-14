#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct X {
  int a;
};

typedef struct list_node *list;

struct list_node {
  list next;
  list *ptr;
  struct X *value;
};

list f(list lst) {
  return lst->next;
}

int main(void) {
  return 0;
}
