#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <signal.h>
#include <stdlib.h>

void terminate(int sig) {
  char buf[64] = {0};
  exit(1);
}

int main(int argc, char **argv) {
  signal(0, terminate);

  return 0;
}
