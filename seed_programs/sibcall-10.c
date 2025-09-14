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

static ATTR void recurser_void1(void);
static ATTR void recurser_void2(void);
extern void track(void);
static volatile int v;

int n = 0;
int main() {
  recurser_void1();
  if (v != 5) {
    abort();
  }
  exit(0);
}

static void __attribute__((noipa)) ATTR recurser_void1(void) {
  if (n == 0 || n == 7 || n == 8) {
    track();
  }

  if (n == 10) {
    return;
  }
  n++;
  recurser_void2();
}

static void __attribute__((noipa)) ATTR recurser_void2(void) {
  if (n == 0 || n == 7 || n == 8) {
    track();
  }

  if (n == 10) {
    return;
  }
  n++;
  v++;
  recurser_void1();
}

void *trackpoint;

void __attribute__((noipa)) track() {
  char stackpos[1];

  if (n == 0) {
    trackpoint = stackpos;
  } else if (!(n != 7 && n != 8) && trackpoint == stackpos) {
    abort();
  }
}
