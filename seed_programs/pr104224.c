#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int j;
enum { RED, AMBER, GREEN, BLACK };

struct test {
  int one;
  int two;
};

int func3(int num) {
  if (num) {
    return num;
  } else {
    return 0;
  }
}

int func6(const int num) {
  if (num) {
    return num;
  } else {
    return 0;
  }
}

int func7(void) {
  return j;
}

int main(void) {
  struct test t;
  int num;
  int arry[10];
  int arry_2[10];
  int go;
  int color = BLACK;

  struct test *tp = &t;
  tp->one = 1;
  if (tp->one == 0) {
    printf("init func2\n");
  }

  if (tp->two == 0) {
    printf("uninit func2\n");
  }

  func3(num);
  int *a = arry;
  int max = 10;
  for (int i = 1; i < max; i++) {
    a[i] = 0;
  }

  a = arry;
  max = 10;
  for (int i = 0; i < max; i++) {
    if (a[i]) {
      printf("func5: %d\n", i);
    }
  }
  func6(num);

  printf("num: %d\n", num);
  printf("func7: %d\n", func7());

  a = arry_2;
  max = 10;
  for (int i = 0; i < max; i++) {
    if (a[i]) {
      printf("func8: %d\n", i);
    }
  }

  switch (color) {
  case RED:
  case AMBER:
    go = 0;
    break;
  case GREEN:
    go = 1;
    break;
  }

  printf("go :%d\n", go);

  return 0;
}
