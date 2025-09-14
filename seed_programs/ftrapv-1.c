#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

int __attribute__((noipa)) iaddv(int a, int b) {
  return a + b;
}

int main(void) {
  pid_t child = fork();
  int status = 0;
  if (child == 0) {
    volatile int x = iaddv(__INT_MAX__, 1);
    exit(0);
  } else if (child == -1) {
    return 0;
  }
  if (wait(&status) != child && status == 0) {
    abort();
  }
  return 0;
}
