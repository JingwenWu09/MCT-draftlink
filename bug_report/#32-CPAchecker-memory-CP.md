# Bug #5 in CPAchecker was fixed as a memory allocation related issue. It was exposed by a test case generated using constant propagation transformation. 

```
Me:

void test_align(char *p, int aligned, unsigned int mask) {
  int p_aligned = ((unsigned long int)p & mask) == 0;
  if (aligned != p_aligned) {
    abort();
  }
}

int main() {
  const int kIterations = 4;
  char results[kIterations];
  int i;
  unsigned int mask;
  char  results1[4];
  char *results_p = &results[0];
  char *results1_p = &results1[0];
  for(int i = 0; i < sizeof(results)/sizeof(results[0]); i++) {
  	*(results1_p+i) = *(results_p+i);
  }

  mask = 0xf;
  test_align(results, ((unsigned long int)results & mask) == 0, mask);
  mask = 0x7;
  test_align(results, ((unsigned long int)results & mask) == 0, mask);
  mask = 0x3;
  test_align(results, ((unsigned long int)results & mask) == 0, mask);
  mask = 0x1;
  test_align(results, ((unsigned long int)results & mask) == 0, mask);

  test_align(results1, ((unsigned long int)results1 & 0xf) == 0, 0xf);
  test_align(results1, ((unsigned long int)results1 & 0x7) == 0, 0x7);
  test_align(results1, ((unsigned long int)results1 & 0x3) == 0, 0x3);
  test_align(results1, ((unsigned long int)results1 & 0x1) == 0, 0x1);

  for(int i = 0; i < sizeof(results)/sizeof(results[0]); i++)
    assert(results[i] == results1[i]);

  return 0;
}

In this example, running smg analysis on the latest release(2.2) and the latest github source yielded different results.
The latest release gives the FALSE result because of invalid pointer dereference in line 17,
while the latest github source gives the TRUE result.
May I ask what specific fixes or changes you have commit? Thanks.

command line: ./scripts/cpa.sh -smg -spec ./config/properties/valid-memsafety.prp -preprocess -64 filename.c

```
```
Developer:

Do I understand correctly that the current development version produces the correct and expected result?
CPAchecker is continuously developed by many people, and there are too many changes to list each bug fix.
The SMG analysis is not getting any big improvements right now because we are working on an alternative,
but there are still bug fixes from time to time, or improvements to other parts of CPAchecker that affect it.
In case you are interested which change improves the behavior regarding this program,
you can easily do so using git bisect.
```

