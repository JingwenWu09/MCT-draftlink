#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);
extern void exit(int);

#ifdef __mips
#define ATTR __attribute__((nomips16))
#else
#define ATTR
#endif

static ATTR void recurser_void1(int);
static ATTR void recurser_void2(int);
extern void track(int);

int main() {
  recurser_void1(0);
  exit(0);
}

static void __attribute__((noipa)) ATTR recurser_void1(int n) {
  if (n == 0 || n == 7 || n == 8) {
    track(n);
  }

  if (n == 10) {
    return;
  }

  recurser_void2(n + 1);
}

static void __attribute__((noipa)) ATTR recurser_void2(int n) {
  if (n == 0 || n == 7 || n == 8) {
    track(n);
  }

  if (n == 10) {
    return;
  }

  recurser_void1(n + 1);
}

void *trackpoint;

void __attribute__((noipa)) track(int n) {
  char stackpos[1];

  if (n == 0) {
    trackpoint = stackpos;
  } else if (!(n != 7 && n != 8) && trackpoint == stackpos) {
    abort();
  }
}
