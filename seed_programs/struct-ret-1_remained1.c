#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern void exit(int);
struct point_t {
  int x;
  int y;
};

int main(int argc, char *argv[]);
int printPoints(struct point_t a, struct point_t b);
struct point_t toPoint(int x1, int y1);

int main(int argc, char *argv[]) {

  if (printPoints(toPoint(0, 0), toPoint(1000, 1000)) != 1) {
    abort();
  } else {
    exit(0);
  }

  return 0;
}

int printPoints(struct point_t a, struct point_t b) {
  if (a.x != 0 || a.y != 0 || b.x != 1000 || b.y != 1000) {
    return 0;
  } else {
    return 1;
  }
}

struct point_t toPoint(int x1, int y1) {
  struct point_t p;

  p.x = x1;
  p.y = y1;

  return p;
}
