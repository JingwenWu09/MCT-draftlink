# Bug #17 in CPAchecker was fixed as a lifetime model related issue. It was exposed by a test case generated using dead code elimination transformation. 

```
Me:

struct S {int *p;};
struct S f1(int *pa) {
  struct S s; int c = 'd'; 
  char buf[4] = {170, 5};
  unsigned char *b = buf;
  unsigned long v = 0;
  if (!c) {
    v = ((unsigned long)b[0]<<8)
    +b[1]; v >>= 9;
  } else s.p = pa; 
  return s;
}
struct S f2(int *pa) {
  struct S s; s.p = pa;
  return s;
}
void main() {
  int a = 1, b = a; 
  struct S s1, s2;
  s1 = f1(&a);
  s2 = f2(&b);
  assert (*s1.p == *s2.p);
}

In this case, the SMGAnalysis gave FALSE result with the message valid-deref: invalid pointer dereference in line,
indicating that FALSE result was due to the statement *s1.p = *s2.p; in main() function.
i.e., SMGAnalysis gives invalid pointer message when dealing with a pointer parameter.
Please help me to expain these situations. Thanks.

command line:
./scripts/cpa.sh -smg -spec ./config/properties/valid-memsafety.prp -preprocess -64 <filename.c>

```
```
Developer:

This is indeed a bug and should be TRUE. (For both SMG and SMG2)
Note: we return by value in the first 2 examples, so the stack struct
inside the function call is discarded after its values are copied to the struct in main.
But the values (the pointer in p) is valid and there should not be a unsafe memory error.
I will fix this for SMG2 soon and update here.

```
```
Developer:

This has been fixed for SMG2.
```

