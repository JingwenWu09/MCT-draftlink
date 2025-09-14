#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct object {
  char *addr;
  long type;
};

int nil;
struct object cons1[2] = {{(char *)&nil, 0}, {(char *)&nil, 0}};
struct object cons2[2] = {{(char *)&cons1, 64}, {(char *)&nil, 0}};

struct object bar(struct object blah) {
  abort();
}

struct object foo(struct object x, struct object y) {
  struct object z = *(struct object *)(x.addr);
  if (z.type & 64) {
    y = *(struct object *)(z.addr + sizeof(struct object));
    z = *(struct object *)(z.addr);
    if (z.type & 64) {
      y = bar(y);
    }
  }
  return y;
}

main() {
  struct object x = {(char *)&cons2, 64};
  struct object y = {(char *)&nil, 0};
  struct object three = foo(x, y);
  return 0;
}
