#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);

struct foo_t {
  unsigned int blksz;
  unsigned int bf_cnt;
};

#define _RNDUP(xd, unit) ((((xd) + (unit)-1) / (unit)) * (unit))
#define _RNDDOWN(xd, unit) ((xd) - ((xd) % (unit)))

int main() {
  struct foo_t x;
  long long xx;

  x.blksz = 8192;
  x.bf_cnt = 0;

  struct foo_t *const pxp = &x;
  long long offset = 0;
  unsigned int extent = 4096;
  long long blkoffset = _RNDDOWN(offset, (long long)pxp->blksz);
  unsigned int diff = (unsigned int)(offset - blkoffset);
  unsigned int blkextent = _RNDUP(diff + extent, pxp->blksz);

  if (pxp->blksz < blkextent) {
    return -1LL;
  }

  if (pxp->bf_cnt > pxp->blksz) {
    pxp->bf_cnt = pxp->blksz;
  }
  xx = blkoffset;
  if (xx != 0LL) {
    abort();
  }
  return 0;
}
