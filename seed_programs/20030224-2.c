#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort();

struct __attribute__((packed)) jint16_t {
  short v16;
};

struct node {
  struct jint16_t magic;
  struct jint16_t nodetype;
  int totlen;
} __attribute__((packed));

struct node node, *node_p = &node;

int main() {
  struct node marker = {.magic = (struct jint16_t){0x1985}, .nodetype = (struct jint16_t){0x2003}, .totlen = node_p->totlen};
  if (marker.magic.v16 != 0x1985) {
    abort();
  }
  if (marker.nodetype.v16 != 0x2003) {
    abort();
  }
  return 0;
}
