# Bug #6 in CPAchecker was fixed as a language semantics related issue. It was exposed by a test case generated using constant propagation transformation.

```
Me:

enum foo { FOO, BAR };
int main() {
  int i = 0;
  int flag = 0;
  int flag_1 = flag;
  for (i = FOO; i >= FOO; --i) {
    if (i == -1) {
      flag = 1;
    }
  }
  for (i = 0; i >= 0; --i) {
    if (i == -1) {
      flag_1 = 1;
    }
  }
  assert(flag == flag_1);
  exit(0);
}

Command line:
scripts/cpa.sh -predicateAnalysis--overflow -spec config/properties/no-overflow.prp -preprocess
-64 -setprop cpa.predicate.ignoreIrrelevantVariables=false filename.c

Please help me to explain why it gives the FALSE result, thanks.

```
```
Developer:

Indeed, thanks for reporting this bug! Because we handle enums slightly different
when overflow checking is enabled, handling for this was missing in one place
and thus the value got assumed to be arbitrary.
I am running some tests and then I will commit a fix.
```

