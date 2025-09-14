#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#if DEBUG
extern int puts(const char *);
#else
#define puts(XX)
#endif
extern void abort(void);
extern int strcmp(const char *s1, const char *s2);

#define j(xx, yy) xx + yy
#define k(xx, yy) j(xx + 2, yy +
#define glue(xx, yy) xx##yy
#define xglue(xx, yy) glue(xx, yy)

int q(int x) { return x + 40; }
int B(int x) { return x + 20; }
int foo(int x) { return x + 10; }
int bar(int x, int y) { return x + y; }
int baz(int x, int y) { return x + y; }
int toupper(int x) { return x + 32; }
int M(int x) { return x * 2; }

int main(int argc, char *argv[]) {
#define q(x) x
  if (q(q)(2) != 42) {
    do {
      puts("p");
      abort();
    } while (0);
  }

#define A(x) B(x)
  if (A(A(2)) != 42) {
    do {
      puts("A");
      abort();
    } while (0);
  }

#define E(x) A x
#define F (22)
  if (E(F) != 42) {
    do {
      puts("E(F)");
      abort();
    } while (0);
  }

#define COMMA ,
#define NASTY(a) j(a 37)
  if (NASTY(5 COMMA) != 42) {
    do {
      puts("NASTY");
      abort();
    } while (0);
  }

#define bar(x, y) foo(x(y, 0))
#define apply(x, y) foo(x(y, 22))
#define bam bar
  if (bar(bar, 32) != 42) {
    do {
      puts("bar bar");
      abort();
    } while (0);
  }
  if (bar(bam, 32) != 42) {
    do {
      puts("bar bam");
      abort();
    } while (0);
  }
  if (apply(bar, baz) != 42) {
    do {
      puts("apply bar baz");
      abort();
    } while (0);
  }

#define __tobody(c, f) f(c)
#define toupper(c) __tobody(c, toupper)
  if (toupper(10) != 42) {
    do {
      puts("toupper");
      abort();
    } while (0);
  }

#define M(x) 2 + M(x)
#define stpcpy(a) M(a)
  if (stpcpy(stpcpy(9)) != 42) {
    do {
      puts("stpcpy");
      abort();
    } while (0);
  }

#define B(x) 1 + B(x)
#define C(x) A(x)
  if (C(B(0)) != 42) {
    do {
      puts("C");
      abort();
    } while (0);
  }
    int insn = 6, i = 2, b = 2;
#define XEXP(RTX, N) (RTX * N + 2)
#define PATTERN(INSN) XEXP(INSN, 3)
    if (XEXP(PATTERN(insn), i) != 42) {
      do {
        puts("XEXP (PATTERN)");
        abort();
      } while (0);
    }
    if (XEXP(XEXP(insn, 3), i) != 42) {
      do {
        puts("XEXP (XEXP)");
        abort();
      } while (0);
    }

#define COST(X) XEXP(XEXP(X, 4), 4)
    if (COST(b) != 42) {
      do {
        puts("COST");
        abort();
      } while (0);
    }

#define FORTYTWO "forty"
#define TWO TWO "-two"
    if (strcmp(glue(FORTY, TWO), "forty")) {
      do {
        puts("glue");
        abort();
      } while (0);
    }
    if (strcmp(xglue(FORTY, TWO), "forty-two")) {
      do {
        puts("xglue");
        abort();
      } while (0);
    }

    if (q(42) != 42 || q(42) != 42 || q(42) != 42 || q(42) != 42) {
      do {
        puts("q over multiple lines");
        abort();
      } while (0);
    }

    if (q(1 + q)(1) != 42) {
      do {
        puts("Nested q");
        abort();
      } while (0);
    }

    if (k(1, 4) 35) != 42) {
        do {
          puts("k");
          abort();
        } while (0);
      }

    return 0;
}
