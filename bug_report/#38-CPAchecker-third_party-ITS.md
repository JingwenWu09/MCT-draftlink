# Bug #11 in CPAchecker was confirmed as a third-party dependency related issue. It was exposed by a test case generated using If-else chain to Switch conversion transformation. 

```
Me:

#include<assert.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);

union _D_rep {
  unsigned short rep[4];
  double val;
};

int main() {
  union _D_rep infinit = {{0, 0, 0, 0x70}};
  double tablei[2] = {infinit.val, 23};
  double keyi = 23;

  double *key = &keyi;
  double *table = tablei;
  unsigned i = 0;
  double *deletedEntry = 0;

  //renameUseVarStmt
  double  tablei_1[2];
  double *tablei_p = &tablei[0];
  double *tablei_1_p = &tablei_1[0];
  for(int i = 0; i < sizeof(tablei)/sizeof(tablei[0]); i++) {
  	tablei_1_p[i] = tablei_p[i];
  }
  double * table_1 = &tablei_1[0] + (table - &tablei[0]);
  unsigned int i_1 = i;
  double keyi_1 = keyi;
  double * key_1 =  &keyi_1;
  double * deletedEntry_1 =  0;
  while (1) {
    double *entry = table + i;

    if (*entry == *key) {
      break;
    }

    union _D_rep _D_inf = {{0, 0, 0, 0x70}};
    if (*entry != _D_inf.val) {
      abort();
    }

    union _D_rep _D_inf2 = {{0, 0, 0, 0x70}};
    if (!_D_inf2.val) {
      deletedEntry = entry;
    }

    i++;
  }

  if (1) {
  do {
    double *entry = table_1 + i_1;

    int cond = 0;
    union _D_rep _D_inf;
    union _D_rep _D_inf2;

    if (*entry == *key_1) {
      cond = 1;
    } else if (*entry != (_D_inf = (union _D_rep){{0, 0, 0, 0x70}}).val) {
      cond = 2;
    } else if (!(_D_inf2 = (union _D_rep){{0, 0, 0, 0x70}}).val) {
      cond = 3;
    }

    switch (cond) {
      case 1:
        break;
      case 2:
        abort();
        break;
      case 3:
        deletedEntry_1 = entry;
        break;
      default:
        break;
    }

    i_1++;
  } while (1);
  }

  assert( keyi == keyi_1 ) ;
  double *tablei_cmp = &tablei[0];
  double *tablei_1_cmp = &tablei_1[0];
  for(int i = 0; i < sizeof(tablei)/sizeof(tablei[0]); i++) {
  	assert( tablei_cmp[i] == tablei_1_cmp[i] ) ;
  }
  assert(i == i_1) ;

  if (deletedEntry) {
    *deletedEntry = 0.0;
  }

  return 0;
}

In this case, tthe assert() function is true, which is confirmed by compiling with gcc and clang,
while CPAchecker generates exception in k-induction analysis and sometimes gives the TRUE result.
The specific exception information is follows.

(not (fp.eq (select *double@1 (bvadd |main::tablei_cmp@1| (bvshl ((_ sign_extend 32) |main::i__2@3|) #b0000000000000000000000000000000000000000000000000000000000000011))) (select *double@1 (bvadd |main::tablei_1_cmp@1| (bvshl ((_ sign_extend 32) |main::i__2@3|) #b0000000000000000000000000000000000000000000000000000000000000011)))))
(= |main::tablei_cmp@1| #b0000000000000000000000000000000000000000000000000000000000000000)
(= |main::entry@2| #b0000000000000000000000000000000000000000000000000000000000001000)
(= (fp #b0 #b10000000000 #b0000000000000000000000000000000000000000000000000000) (select *double@1 (bvadd |main::tablei_cmp@1| (bvshl ((_ sign_extend 32) |main::i__2@3|) #b0000000000000000000000000000000000000000000000000000000000000011))))
(= #b0000000000000000000000000000000000000000000000000000000000001000 (bvadd |main::tablei_cmp@1| (bvshl ((_ sign_extend 32) |main::i__2@2|) #b0000000000000000000000000000000000000000000000000000000000000011)))
(fp.eq (select *double@1 (bvadd |main::tablei_cmp@1| (bvshl ((_ sign_extend 32) |main::i__2@2|) #b0000000000000000000000000000000000000000000000000000000000000011))) (select *double@1 (bvadd |main::tablei_1_cmp@1| (bvshl ((_ sign_extend 32) |main::i__2@2|) #b0000000000000000000000000000000000000000000000000000000000000011))))
(= (select *double@1 |main::entry@2|) (fp #b0 #b10000000000 #b0000000000000000000000000000000000000000000000000000))
(= |main::tablei_1_cmp@1| #b0000000000000000000000000000000000000000000000000000000000000000)
Analysis was terminated (Parallel analysis 2:ParallelAlgorithm.runParallelAnalysis, INFO)

Exception in thread "main" java.lang.IllegalArgumentException: MathSAT returned null
	at org.sosy_lab.java_smt.solvers.mathsat5.Mathsat5NativeApi.msat_model_eval(Native Method)
	at org.sosy_lab.java_smt.solvers.mathsat5.Mathsat5Model.evalImpl(Mathsat5Model.java:141)
	at org.sosy_lab.java_smt.solvers.mathsat5.Mathsat5Model.evalImpl(Mathsat5Model.java:35)
	at org.sosy_lab.java_smt.basicimpl.AbstractModel.evaluateImpl(AbstractModel.java:95)
	at org.sosy_lab.java_smt.solvers.mathsat5.Mathsat5Model.getArrayAssignments(Mathsat5Model.java:111)
	at org.sosy_lab.java_smt.solvers.mathsat5.Mathsat5Model.toList(Mathsat5Model.java:66)
	at org.sosy_lab.java_smt.basicimpl.AbstractModel$CachingAbstractModel.asList(AbstractModel.java:116)
	at org.sosy_lab.java_smt.api.BasicProverEnvironment.getModelAssignments(BasicProverEnvironment.java:99)
	at org.sosy_lab.cpachecker.util.predicates.smt.BasicProverEnvironmentView.getModelAssignments(BasicProverEnvironmentView.java:71)
	at org.sosy_lab.cpachecker.core.algorithm.bmc.ProverEnvironmentWithFallback.getModelAssignments(ProverEnvironmentWithFallback.java:213)
	at org.sosy_lab.cpachecker.core.algorithm.bmc.KInductionProver.check(KInductionProver.java:487)
	at org.sosy_lab.cpachecker.core.algorithm.bmc.AbstractBMCAlgorithm.checkStepCase(AbstractBMCAlgorithm.java:537)
	at org.sosy_lab.cpachecker.core.algorithm.bmc.AbstractBMCAlgorithm.run(AbstractBMCAlgorithm.java:470)
	at org.sosy_lab.cpachecker.core.algorithm.bmc.BMCAlgorithm.run(BMCAlgorithm.java:130)
	at org.sosy_lab.cpachecker.core.algorithm.ParallelAlgorithm.runParallelAnalysis(ParallelAlgorithm.java:402)
	at org.sosy_lab.cpachecker.core.algorithm.ParallelAlgorithm.lambda$createParallelAnalysis$3(ParallelAlgorithm.java:346)
	at com.google.common.util.concurrent.TrustedListenableFutureTask$TrustedFutureInterruptibleTask.runInterruptibly(TrustedListenableFutureTask.java:131)
	at com.google.common.util.concurrent.InterruptibleTask.run(InterruptibleTask.java:74)
	at com.google.common.util.concurrent.TrustedListenableFutureTask.run(TrustedListenableFutureTask.java:82)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	at java.base/java.lang.Thread.run(Thread.java:833)


Command: ~/cpachecker/scripts/cpa.sh -kInduction -64 -preprocess
-setprop cpa.predicate.ignoreIrrelevantVariables=false <file_name.c>

```
```
Developer:

This crash is produced by the SMT solver that we use (MathSAT).
It can be triggered reliably with the following command line:

scripts/cpa.sh -config config/components/kInduction/kInduction.properties -preprocess -64 -setprop
cpa.predicate.ignoreIrrelevantVariables=false example.c -spec default

If I dump the solver queries in SMTLib and run the command line binary of MathSAT 5.6.8,
it works fine and produces a model. (MathSAT 5.6.9 segfaults,
but I think this is unrelated because we still use 5.6.8 inside CPAchecker).
So this is the same situation as in #680 where it is difficult to communicate
the bug report to the MathSAT developers because it happens only inside CPAchecker and not outside.

```
```
Developer:

This particular case seems to be solved in the current development version of CPAchecker
due to the upgrade to MathSAT 5.6.10. 

```

