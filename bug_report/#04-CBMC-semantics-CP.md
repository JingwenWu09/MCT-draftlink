# Bug #4 in CBMC was confirmed as a language semantics related issue. It was exposed by a test case generated using constant propagation transformation. 

```
Me:

#include <assert.h>

struct list {
  struct list *n;
};

struct obj {
  int n;
  struct list l;
} _o;

struct list _l = {.n = &_o.l};

int main(int argc, char *argv[]) {
  struct obj *o1 = &_o;
  struct obj *o2 = &_o;
  _o.l.n = &_l;

  int count1 = 0;
  int count2 = 0;
  while (&o1->l != &_l) {
    count1++;
    o1 = ((struct obj *)((const char *)(o1->l.n) - (const char *)&((struct obj *)0)->l));
  }
  while (&o2->l != &_l) {
    count2++;
    const char *base = (const char *)o2->l.n;
    const char *offset = (const char *)&((struct obj *)0)->l;
    o2 = (struct obj *)(base - offset);
  }
  assert(count1 == count2);

  return 0;
}

In this case, the value of count is 1, which is verified in both gcc and clang. Why does
cbmc keep running and seemingly unstoppable? Please help me to explain this situation, thanks.

CBMC version: 5.88.0
Operating system: Ubuntu 22.04, macOS
Command line: cbmc example.c
```
```
Developer:

How would CBMC know that the loop is finite?

CBMC proceeds in two phases:

Symbolic execution with some imprecise analysis: This generates the SMT formula and may sometimes
figure out that the loop condition becomes false. If it doesn't then it just continues unwinding
the loop until you run out of memory. To prevent this, you need to bound the loop yourself by adding
--unwind options.

Once it has got a (finite) SMT formula it solves that formula with bit-precise precision,
which will determine whether or not an assertion fails within the number of unwindings that
the formula contains. When an assertion after the loop exit doesn't fail then you may want to understand
whether this is due to the assertion being always true or due to the loop never terminating.
Rerunning with the --unwinding-assertions option will tell you that.
```
```
Me:

My understanding is that when the unwind is set to a value greater than the number of loop executions
and assert() is true, the result of the unwinding assertion is SUCCESS. So in this example,
I use count to keep track of how many times the loop was executed, and I get count = 1 in gcc and clang.
Running cbmc <filename.c> --unwind n --unwinding-assertions, the result shows that
assert(count == 1) is SUCCESS only if n = 1 || n = 2, but the unwinding assertion is always FAILURE
regardless of the value of n. Is my understanding of unwind and unwinding-assertions wrong?
Or is there something wrong with cbmc's analysis of this example?

```
```
Developer:

Your example invokes undefined behaviour according to the standard,
as the expression ((struct obj *)0)->l dereferences a null pointer.
This can be shown if you run your example with --unwind 10 --pointer-check.
In the face of undefined behaviour your reasoning about other behaviour,
such as the number of times a loop is executed may not hold.
```



