#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct font_hints_s {
  int axes_swapped;
  int x_inverted, y_inverted;
};
struct gs_fixed_point_s {
  long x, y;
};

int main() {
  static struct font_hints_s fhi[] = {{0, 1, 0}, {0, 0, 1}, {0, 0, 0}};
  static struct gs_fixed_point_s gsf[] = {{0x30000, 0x13958}, {0x30000, 0x18189}, {0x13958, 0x30000}, {0x18189, 0x30000}};

  const struct font_hints_s *fh = fhi;
  const struct gs_fixed_point_s *p0 = gsf;
  const struct gs_fixed_point_s *p1 = gsf + 1;
  int returnValue1;
  long dx = p1->x - p0->x;
  long dy = p1->y - p0->y;
  long adx, ady;
  int xi = fh->x_inverted, yi = fh->y_inverted;
  int hints;
  if (xi) {
    dx = -dx;
  }
  if (yi) {
    dy = -dy;
  }
  if (fh->axes_swapped) {
    long t = dx;
    int ti = xi;
    dx = dy, xi = yi;
    dy = t, yi = ti;
  }
  adx = dx < 0 ? -dx : dx;
  ady = dy < 0 ? -dy : dy;
  if (dy != 0 && (adx <= ady >> 4)) {
    hints = dy > 0 ? 2 : 1;
    if (xi) {
      hints ^= 3;
    }
  } else if (dx != 0 && (ady <= adx >> 4)) {
    hints = dx < 0 ? 8 : 4;
    if (yi) {
      hints ^= 12;
    }
  } else {
    hints = 0;
  }
  returnValue1 = hints;

  fh = fhi + 1;
  p0 = gsf + 2;
  p1 = gsf + 3;
  int returnValue2;
  dx = p1->x - p0->x;
  dy = p1->y - p0->y;
  adx = 0;
  ady = 0;
  xi = fh->x_inverted;
  yi = fh->y_inverted;
  hints = 0;
  if (xi) {
    dx = -dx;
  }
  if (yi) {
    dy = -dy;
  }
  if (fh->axes_swapped) {
    long t = dx;
    int ti = xi;
    dx = dy, xi = yi;
    dy = t, yi = ti;
  }
  adx = dx < 0 ? -dx : dx;
  ady = dy < 0 ? -dy : dy;
  if (dy != 0 && (adx <= ady >> 4)) {
    hints = dy > 0 ? 2 : 1;
    if (xi) {
      hints ^= 3;
    }
  } else if (dx != 0 && (ady <= adx >> 4)) {
    hints = dx < 0 ? 8 : 4;
    if (yi) {
      hints ^= 12;
    }
  } else {
    hints = 0;
  }
  returnValue2 = hints;

  fh = fhi + 2;
  p0 = gsf + 2;
  p1 = gsf + 3;
  int returnValue3;
  dx = p1->x - p0->x;
  dy = p1->y - p0->y;
  adx = 0;
  ady = 0;
  xi = fh->x_inverted;
  yi = fh->y_inverted;
  hints = 0;
  if (xi) {
    dx = -dx;
  }
  if (yi) {
    dy = -dy;
  }
  if (fh->axes_swapped) {
    long t = dx;
    int ti = xi;
    dx = dy, xi = yi;
    dy = t, yi = ti;
  }
  adx = dx < 0 ? -dx : dx;
  ady = dy < 0 ? -dy : dy;
  if (dy != 0 && (adx <= ady >> 4)) {
    hints = dy > 0 ? 2 : 1;
    if (xi) {
      hints ^= 3;
    }
  } else if (dx != 0 && (ady <= adx >> 4)) {
    hints = dx < 0 ? 8 : 4;
    if (yi) {
      hints ^= 12;
    }
  } else {
    hints = 0;
  }
  returnValue3 = hints;

  if (returnValue1 != 1 || returnValue2 != 8 || returnValue3 != 4) {
    abort();
  }
  exit(0);
}
