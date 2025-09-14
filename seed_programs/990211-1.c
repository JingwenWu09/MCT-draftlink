#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort();

#define N 77

void func(int i) {

  if ((i < 0) && (i >= 0)) {
    abort();
  }
  if ((i > 0) && (i <= 0)) {
    abort();
  }
  if ((i >= 0) && (i < 0)) {
    abort();
  }
  if ((i <= 0) && (i > 0)) {
    abort();
  }

  if ((i < N) && (i >= N)) {
    abort();
  }
  if ((i > N) && (i <= N)) {
    abort();
  }
  if ((i >= N) && (i < N)) {
    abort();
  }
  if ((i <= N) && (i > N)) {
    abort();
  }

  if (!((i < 0) || (i >= 0))) {
    abort();
  }
  if (!((i > 0) || (i <= 0))) {
    abort();
  }
  if (!((i >= 0) || (i < 0))) {
    abort();
  }
  if (!((i <= 0) || (i > 0))) {
    abort();
  }

  if (!((i < N) || (i >= N))) {
    abort();
  }
  if (!((i > N) || (i <= N))) {
    abort();
  }
  if (!((i >= N) || (i < N))) {
    abort();
  }
  if (!((i <= N) || (i > N))) {
    abort();
  }

  return;
}

int main() {
  func(0);
  func(1);
  return 0;
}
