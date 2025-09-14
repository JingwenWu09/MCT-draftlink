# Bug #1 in CPAchecker was confirmed as a C standard library related issue. It was exposed by a test case generated using dead code elimination transformation. 

```
Me:

void main() {
  _Complex double a = 1.0 + 2.0I;
  _Complex double b = 3.0 + 4.0I;
  _Complex double c = a;
  
  if (creal(a) == creal(b)
    && cimag(a) == cimag(b)) {
    a = a + b;
  }
  
  assert (creal(a) == creal(c));
  
  assert (cimag(a) == cimag(c));
}

I used .scripts/cpa.sh -kInduction -preprocess -64 <filename.c> to verify the two examples.
It gave the UNKNOWN reslut with the following message :
Error: line 7: Unrecognized C code (left operand of assignment has to be a variable): __real__ a (line was originally __real__ a = real;) (FunctionPointerTransferRelation.getLeftHandSide, SEVERE)

```
```
Developer:

CPAchecker has some partial support for types for complex numbers, but not enough to verify them.
It should typically be able to parse code with complex numbers,
but your second example shows that there seems to be some problem in the parser that we use (Eclipse CDT).
```



