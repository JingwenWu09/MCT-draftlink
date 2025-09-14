#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

typedef char ACS;
typedef char LSM;
typedef char PANEL;
typedef char DRIVE;
struct LSMID {
  ACS acs;
  LSM lsm;
};
struct PANELID {
  struct LSMID lsm_id;
  PANEL panel;
};
struct DRIVEID {
  struct PANELID panel_id;
  DRIVE drive;
};

void sub(struct DRIVEID driveid) {
  if (driveid.drive != 1) {
    abort();
  }
  if (driveid.panel_id.panel != 2) {
    abort();
  }
  if (driveid.panel_id.lsm_id.lsm != 3) {
    abort();
  }
  if (driveid.panel_id.lsm_id.acs != 4) {
    abort();
  }
}

int main(void) {
  struct DRIVEID driveid;

  driveid.drive = 1;
  driveid.panel_id.panel = 2;
  driveid.panel_id.lsm_id.lsm = 3;
  driveid.panel_id.lsm_id.acs = 4;

  sub(driveid);

  return 0;
}
