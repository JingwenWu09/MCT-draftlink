#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct decision {
  char enforce_mode;
  struct decision *next;
};

main() {
  struct decision *p = 0;

	while(p){
		p->enforce_mode = 0;
	}

  exit(0);
}
