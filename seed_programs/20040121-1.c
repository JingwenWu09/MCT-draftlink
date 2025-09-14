#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

int main() {
  char *p = "a", *q = "b";
  if (p) {
    char *first = p;
    char *last = q;
    char *returnValue;
    int y;
    if (!first) {
      returnValue = last;
      if (returnValue) {
		    return 0;
		  }
    }
    if (!last) {
      returnValue = first;
      if (returnValue) {
		    return 0;
		  }
    }
    if (*first == 'a') {
      int x = (first != 0) + (last != 0);
      if (x == 2) {
        returnValue = "a";
      }
    }
    returnValue = 0;

    if (returnValue) {
      return 0;
    }
  }
}
