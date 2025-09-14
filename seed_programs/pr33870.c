#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);

typedef unsigned char u8;
struct PgHdr {
  unsigned int pgno;
  struct PgHdr *pNextHash, *pPrevHash;
  struct PgHdr *pNextFree, *pPrevFree;
  struct PgHdr *pNextAll;
  u8 inJournal;
  short int nRef;
  struct PgHdr *pDirty, *pPrevDirty;
  unsigned int notUsed;
};

int main() {
  struct PgHdr ai[5];
  struct PgHdr *pi;
  ai[0].pgno = 5;
  ai[0].pDirty = &ai[1];
  ai[1].pgno = 4;
  ai[1].pDirty = &ai[2];
  ai[2].pgno = 1;
  ai[2].pDirty = &ai[3];
  ai[3].pgno = 3;
  ai[3].pDirty = 0;

  struct PgHdr *pIn = &ai[0];
  struct PgHdr *a[25], *p;
  int i;
  __builtin_memset(a, 0, sizeof(a));
  while (pIn) {
    p = pIn;
    pIn = p->pDirty;
    p->pDirty = 0;
    for (i = 0; i < 25 - 1; i++) {
      if (a[i] == 0) {
        a[i] = p;
        break;
      } else {
        struct PgHdr *pA = a[i];
        struct PgHdr *pB = p;
        struct PgHdr result;
        struct PgHdr *pTail;
        pTail = &result;
        while (pA && pB) {
          if (pA->pgno < pB->pgno) {
            pTail->pDirty = pA;
            pTail = pA;
            pA = pA->pDirty;
          } else {
            pTail->pDirty = pB;
            pTail = pB;
            pB = pB->pDirty;
          }
        }
        if (pA) {
          pTail->pDirty = pA;
        } else if (pB) {
          pTail->pDirty = pB;
        } else {
          pTail->pDirty = 0;
        }
        p = result.pDirty;
        a[i] = 0;
      }
    }
    if (i == 25 - 1) {
      struct PgHdr *pA = a[i];
      struct PgHdr *pB = p;
      struct PgHdr result;
      struct PgHdr *pTail;
      pTail = &result;
      while (pA && pB) {
        if (pA->pgno < pB->pgno) {
          pTail->pDirty = pA;
          pTail = pA;
          pA = pA->pDirty;
        } else {
          pTail->pDirty = pB;
          pTail = pB;
          pB = pB->pDirty;
        }
      }
      if (pA) {
        pTail->pDirty = pA;
      } else if (pB) {
        pTail->pDirty = pB;
      } else {
        pTail->pDirty = 0;
      }
      a[i] = result.pDirty;
    }
  }
  p = a[0];
  for (i = 1; i < 25; i++) {
    struct PgHdr *pA = p;
    struct PgHdr *pB = a[i];
    struct PgHdr result;
    struct PgHdr *pTail;
    pTail = &result;
    while (pA && pB) {
      if (pA->pgno < pB->pgno) {
        pTail->pDirty = pA;
        pTail = pA;
        pA = pA->pDirty;
      } else {
        pTail->pDirty = pB;
        pTail = pB;
        pB = pB->pDirty;
      }
    }
    if (pA) {
      pTail->pDirty = pA;
    } else if (pB) {
      pTail->pDirty = pB;
    } else {
      pTail->pDirty = 0;
    }
    p = result.pDirty;
  }
  pi = p;
  if (pi->pDirty == pi) {
    abort();
  }
  return 0;
}
