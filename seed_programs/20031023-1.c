#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <limits.h>
#define ASIZE 4096

extern void abort(void);

int __attribute__((noinline)) bar(void) {
  char s[ASIZE];
  s[0] = 'a';
  s[ASIZE - 1] = 'b';
  char *sp = s;
  if (!sp) {
		sp = s;
		if (!sp) {
		  return 0;
		}
		if (sp[0] != 'a') {
		  abort();
		}
		sp += ASIZE - 1;
		if (sp[0] != 'b') {
		  abort();
		}
		return 0;
  }
  if (sp[0] != 'a') {
    abort();
  }
  sp += ASIZE - 1;
  if (sp[0] != 'b') {
    abort();
  }

  sp = s;
  if (!sp) {
    return 0;
  }
  if (sp[0] != 'a') {
    abort();
  }
  sp += ASIZE - 1;
  if (sp[0] != 'b') {
    abort();
  }
  return 0;
}

int __attribute__((noinline)) baz(long i) {
  if (i) {
    return 1;
  } else {
    char s[ASIZE];
    s[0] = 'a';
    s[ASIZE - 1] = 'b';
    char *sp = s;
    if (!sp) {
			sp = s;
		  if (!sp) {
		    return 1;
		  }
		  if (sp[0] != 'a') {
		    abort();
		  }
		  sp += ASIZE - 1;
		  if (sp[0] != 'b') {
		    abort();
		  }
		  return 1;
    }
    if (sp[0] != 'a') {
      abort();
    }
    sp += ASIZE - 1;
    if (sp[0] != 'b') {
      abort();
    }

    sp = s;
    if (!sp) {
      return 1;
    }
    if (sp[0] != 'a') {
      abort();
    }
    sp += ASIZE - 1;
    if (sp[0] != 'b') {
      abort();
    }
    return 1;
  }
}

int main(void) {
  if (bar()) {
    abort();
  }
  if (baz(0) != 1) {
    abort();
  }
  if (baz(1) != 1) {
    abort();
  }
  return 0;
}
