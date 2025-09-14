#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct bfd_link_hash_table {
  int hash;
};

struct foo_link_hash_table {
  struct bfd_link_hash_table root;
  int *dynobj;
  int *sgot;
};

struct foo_link_info {
  struct foo_link_hash_table *hash;
};

extern void abort(void);

int __attribute__((noinline)) foo_create_got_section(int *abfd, struct foo_link_info *info) {
  info->hash->sgot = abfd;
  return 1;
}

struct foo_link_info link_infoi;
struct foo_link_hash_table hashi;
int abfdi;

int main() {
  link_infoi.hash = &hashi;

  int *abfd = &abfdi;
  struct foo_link_info *info = &link_infoi;
  int *returnValue;
  struct foo_link_hash_table *hash = info->hash;
  int *got;
  int *dynobj;

  got = hash->sgot;
  if (!got) {
    dynobj = hash->dynobj;
    if (!dynobj) {
      hash->dynobj = dynobj = abfd;
    }
    if (!foo_create_got_section(dynobj, info)) {
      returnValue = 0;
      if (returnValue != &abfdi) {
				abort();
			}
			return 0;
    }

    got = hash->sgot;
  }
  returnValue = got;

  if (returnValue != &abfdi) {
    abort();
  }
  return 0;
}
