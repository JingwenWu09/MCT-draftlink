#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int __attribute__((returns_twice, noipa)) x() {
  return 0;
}
void __attribute__((noipa)) ar() {
}
void __attribute__((noipa)) as() {
  __builtin_abort();
}
int a1, a2, a3;
void __attribute__((noipa)) v(int init) {
  if (!init) {
    as();
    if (a1) {
      if (!init) {
				as();
			}
    }
    x();
  }
  ar();
  if (!init) {
    as();
  }
}

int main() {
  v(1);
  return 0;
}
