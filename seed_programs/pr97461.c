#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define _GNU_SOURCE

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

static int malloc_depth = 0;

static char memory[128 * 1024];
static size_t memory_p = 0;

void f1(void) {
}
void f2(void) {
}

typedef void (*fun_t)(void);
static const fun_t funs[2] = {
    f1,
    f2,
};

static void *malloc_impl(size_t size) {
  void *r = &memory[memory_p];

#define ROUND_UP_FOR_16B_ALIGNMENT(x) ((x + 15) & (-16))

  memory_p += ROUND_UP_FOR_16B_ALIGNMENT(size);

  funs[size % 2]();
  return r;
}

void *malloc(size_t size) {

  if (malloc_depth != 0) {
    __builtin_trap();
  }

  ++malloc_depth;
  void *r = malloc_impl(size);
  --malloc_depth;
  return r;
}

void *calloc(size_t nmemb, size_t size) {

  if (malloc_depth != 0) {
    __builtin_trap();
  }

  ++malloc_depth;
  void *r = malloc_impl(size * nmemb);
  memset(r, 0, size * nmemb);
  --malloc_depth;
  return r;
}

void free(void *ptr) {
}

int main() {
  void *p = malloc(8);
  return p != 0 ? 0 : 1;
}
