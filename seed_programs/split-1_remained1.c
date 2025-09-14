#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>
#include <sys/resource.h>
#include <sys/types.h>

static void use_buffer(char *buf) __attribute__((noinline));
static void use_buffer(char *buf) {
  buf[0] = '\0';
}

static void down(int i) {
  char buf[1000];

  if (i > 0) {
    use_buffer(buf);
    down(i - 1);
  }
}

int main(void) {
  struct rlimit r;

  r.rlim_cur = 8192 * 1024;
  r.rlim_max = 8192 * 1024;
  if (setrlimit(RLIMIT_STACK, &r) != 0) {
    abort();
  }
  down(1000);
  return 0;
}
