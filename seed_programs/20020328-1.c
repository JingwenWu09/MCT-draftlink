#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int b = 0;

func() {
}

void testit(int x) {
  if (x != 20) {
    abort();
  }
}

int main()

{
  int a = 0;

  if (b) {
    func();
  }

  testit((a + 23) & 0xfffffffc);
  exit(0);
}
