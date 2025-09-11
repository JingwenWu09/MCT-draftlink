# Bug #6 in SeaHorn was confirmed as a language semantics related issue. It was exposed by a test case generated using operator strength reduction transformation. 

```
Me:

unsigned int a[2] = {2, 0}, b[2];
void f1(unsigned int *sp, int d){
  int i = 0; double n = 1.01;
  unsigned int *p, *top; 
  top = sp; sp -= 1;
  for (p = sp; p <= top; p++)
    if (*p < 2 && d) (*p) = (i * 8) - n;
}
void f2(unsigned int *sp, int d){
  int i = 0; double n = 1.01;
  unsigned int *p, *top; 
  top = sp; sp -= 1;
  for (p = sp; p <= top; p++)
    if (*p < 2 && d) (*p) = (i << 3) - n; 
}
void main() {
  memcpy (b, a, sizeof(b));
  f1(a + 1, 0); f2(b + 1, 1);
  assert (a[1] == b[1]); 
}

In this case, I run sea bpf -m64 example.c and SeaHorn gave the sat result.
So I ran the following commands sea bpf --cex=witness.ll example.c,
sea exe -m64 -g example.c witness.ll -o example.exe ,
./example.exe in turn to see the executable counterexample.
But the .exe dit not have any output on the terminal, that is,
I did not see the information "[sea] __VERIFIER_error was executed".

Then I arbitrarily changed the value of n (e.g. 2.0, 343.00, 4.123, etc.)
and found that when the fractional part of n is 0,
the result of the command is unsat, otherwise, the result is sat.

I have the following questions:

For the sat result of example.c, why does the executable counterexample not give
the message "[sea] __VERIFIER_error was executed"?
Is it because SeaHorn checks for something other than assert(); statement in the code?

For this example involving type conversion, why does SeaHorn validate differently on
whether the fractional part of a floating-point number is zero?

Version: seahorn 14.0.0
OS: ubuntu 22.04
```
```
Developer:

I think it has to do again with undefined behavior (UB).
The reason why SeaHorn seems to deal with UB differently than Clang is that
SeaHorn runs always Clang with -O1 and then it performs its own LLVM optimizations.
One of these optimizations replaces undef LLVM values with non-deterministic values.
This option is called promote-nondet-undef and it's set to true by default.
This option was originally designed to deal with programs from SV-COMP
but it should be probably set to false by default.
I also noticed that the option cannot be set via python scripts (sea script)
and it's only accessible via the seapp tool executable.
To get the same behavior than Clang, update SeaHorn and use the command sea bpf -m64 --promote-nondet-undef=false.
Additionally, Seahorn does not support floating points.
My guess is that when n=1.0 LLVM cast it to an integer.
```



