#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <assert.h>
#include <stdio.h>
#include <string.h>

static inline __attribute__((__artificial__)) void *a(char *c, const char *d, long n) {
  return __builtin___memcpy_chk(c, d, n, __builtin_object_size(c, 0));
}
struct sb_t {
  char *data;
  int len;
};
const char __sb_slop[1];

char buf[5];
struct sb_t l = {.data = buf, .len = 0};
void o() {
  char *data = "abcd";
  struct sb_t h = l;
  struct sb_t *c = &h;
  if (c->data != __sb_slop) {
    c->data[0] = 0;
  } else {
    assert(c->data[0] == 0);
  }
  a(h.data, data, strlen(data));
  printf("%s\n", h.data);
  printf("%d\n", h.data == __sb_slop);
  printf("%d\n", h.data == buf);
  c = &h;
  if (c->data != __sb_slop) {
    c->data[0] = 0;
  } else {
    assert(c->data[0] == 0);
  }
}
int main(void) {
  o();
  return 0;
}
