#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);

typedef unsigned char u8;
struct PgHdr {
  int y;
  struct X{
    unsigned int pgno;
    struct PgHdr *pNextHash, *pPrevHash;
    struct PgHdr *pNextFree, *pPrevFree;
    struct PgHdr *pNextAll;
    u8 inJournal;
    short int nRef;
    struct PgHdr *pDirty, *pPrevDirty;
    unsigned int notUsed;
  } x;
};

int main() {
  struct PgHdr **xx;
  volatile int vx;

  struct PgHdr ai[5];
  struct PgHdr *pi;
  ai[0].x.pgno = 5;
  ai[0].x.pDirty = &ai[1];
  ai[1].x.pgno = 4;
  ai[1].x.pDirty = &ai[2];
  ai[2].x.pgno = 1;
  ai[2].x.pDirty = &ai[3];
  ai[3].x.pgno = 3;
  ai[3].x.pDirty = 0;

  struct PgHdr *pIn = &ai[0];
  struct PgHdr *a[25], *p;
  int i;
  __builtin_memset(a, 0, sizeof(a));
  while (pIn) {
    p = pIn;
    pIn = p->x.pDirty;
    p->x.pDirty = 0;
    for (i = 0; i < 25 - 1; i++) {
      if (a[i] == 0) {
        a[i] = p;
        break;
      } else {
        struct PgHdr *pA = a[i];
        struct PgHdr *pB = p;
        struct PgHdr result;
        struct PgHdr *pTail;
        xx = &result.x.pDirty;
        pTail = &result;
        while (pA && pB) {
          if (pA->x.pgno < pB->x.pgno) {
            pTail->x.pDirty = pA;
            pTail = pA;
            pA = pA->x.pDirty;
          } else {
            pTail->x.pDirty = pB;
            pTail = pB;
            pB = pB->x.pDirty;
          }
          vx = (*xx)->y;
        }
        if (pA) {
          pTail->x.pDirty = pA;
        } else if (pB) {
          pTail->x.pDirty = pB;
        } else {
          pTail->x.pDirty = 0;
        }
        p = result.x.pDirty;
        a[i] = 0;
        a[i] = 0;
      }
    }
    if (i == 25 - 1) {
      struct PgHdr *pA = a[i];
      struct PgHdr *pB = p;
      struct PgHdr result;
      struct PgHdr *pTail;
      xx = &result.x.pDirty;
      pTail = &result;
      while (pA && pB) {
        if (pA->x.pgno < pB->x.pgno) {
          pTail->x.pDirty = pA;
          pTail = pA;
          pA = pA->x.pDirty;
        } else {
          pTail->x.pDirty = pB;
          pTail = pB;
          pB = pB->x.pDirty;
        }
        vx = (*xx)->y;
      }
      if (pA) {
        pTail->x.pDirty = pA;
      } else if (pB) {
        pTail->x.pDirty = pB;
      } else {
        pTail->x.pDirty = 0;
      }
      a[i] = result.x.pDirty;
    }
  }
  p = a[0];
  for (i = 1; i < 25; i++) {
    struct PgHdr *pA = p;
    struct PgHdr *pB = a[i];
    struct PgHdr result;
    struct PgHdr *pTail;
    xx = &result.x.pDirty;
    pTail = &result;
    while (pA && pB) {
      if (pA->x.pgno < pB->x.pgno) {
        pTail->x.pDirty = pA;
        pTail = pA;
        pA = pA->x.pDirty;
      } else {
        pTail->x.pDirty = pB;
        pTail = pB;
        pB = pB->x.pDirty;
      }
      vx = (*xx)->y;
    }
    if (pA) {
      pTail->x.pDirty = pA;
    } else if (pB) {
      pTail->x.pDirty = pB;
    } else {
      pTail->x.pDirty = 0;
    }
    p = result.x.pDirty;
  }

  pi = p;
  if (pi->x.pDirty == pi) {
    abort();
  }
  return 0;
}
