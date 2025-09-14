# Model Checker Testing
Based on the metamorphic testing paradigm, we explore
the usefulness of optimization-guided equivalence transformations for validating software model checkers in this paper. In particular, we propose a general testing approach that involves four major steps: 
1) perform program analysis to select particular code snippets that meet specific transformation conditions;
2) apply optimization-guided equivalence transformations to the selected code snippets to get new code snippets;
3) embed the equivalence relations between variables in the original and transformed code snippets as properties to be verified within the program (i.e., create effective test cases);
4) validate the test programs with the model checker and compare the actual verification results with the expected ones. If discrepancies are found, it indicates potential bugs in the software model checker.
The detailed process is illustrated in the figure below. 

<img src="./workflow.jpg" alt="Workflow image" width="600" />

We use GCC regression test cases as seed programs (given in the [seed_programs](https://github.com/Elowen-jjw/MCT-draftlink/tree/main/seed_programs) folder). 

# Structure of the project

```
Core operations are under the main folders:
|-- model_checker_test
| |-- TestCBMC # CBMC workflow implementation
| | |-- Main/Main.java # CBMC entry point
| | |-- Tools/TestCBMC.java # CBMC command executor
| |-- TestCPAchecker # CPAchecker workflow implementation
| | |-- Main/Main.java # CPAchecker entry point
| | |-- Tools/TestCPAchecker.java # CPAchecker command executor
| |-- TestSeahorn_singleton # SeaHorn workflow implementation
| | |-- Main/Main.java # SeaHorn entry point
| | |-- Tools/TestSeahorn.java # SeaHorn command executor

|-- test_case_generation
| |-- ASTInformation # The AST analysis implementation directory
| |-- Condition # The conditional structures transformation implementation directory
| |-- DataFlow # The dataflow transformation implementation directory
| |-- test # The test-case generation overall process execution implemention directory
| | |-- Mutate.java  # Entry point for test-case generation
```

# Usage
Our implementation fully automates the workflow described above.  
At a high level it consists of three stages:

1. **Test-case generation** – Starting from a corpus of seed programs, we locate transformable code snippets and apply *optimization-guided equivalence transformations* to obtain semantically equivalent variants (the test cases).  
2. **Model-checker execution** – Each test case is verified by several model checkers (CPAchecker, CBMC, SeaHorn). For a given tool we can run multiple configurations; each run produces a result txt file.  
3. **Oracle comparison & bug reporting** – The actual verification results are compared with the expected results. Any discrepancy is written to `model-checker-false.txt`, which we later inspect manually and turn into bug reports.

Steps 1-3 below tell you **how to prepare the environment**; Step 4 explains **how to launch the workflow**.

### Step 1: Install necessary packages

- Ubuntu 20+
- Java 17+
- CPAchecker (Please install it following [CPAchecker](https://cpachecker.sosy-lab.org/download.php))
- CBMC (Please install it following [CBMC](https://github.com/diffblue/cbmc))
- SeaHorn (Please install it following [SeaHorn](https://github.com/seahorn/seahorn))
- Clang 14+
- Copy file `libsigar-amd64-linux.so` into `/usr/lib` and `/usr/lib64`
  
> **Tip :** For CPAchecker/CBMC/SeaHorn follow the official installation guides; be sure each tool is on your `$PATH`.

### Step 2: Verify Clang AST support

The project relies on **Clang LibTooling** to analyse the AST, so we first check that your Clang build can dump ASTs correctly.

Assume the following program, test.c:
```
#include <stdio.h>

int add(int a, int b) {
  return a + b;
}

void main() {
  int x = 3;
  int y = 4;
  printf("%d\n", add(x, y));
}
```

The command `clang -fsyntax-only -Xclang -ast-dump test.c -w -Xanalyzer -analyzer-disable-all-checking` is executed, and if the following output occurs, then there is no issue.
```
|-FunctionDecl 0x55f86a0e1450 <test.c:3:1, line:5:1> line:3:5 used add 'int (int, int)'
| |-ParmVarDecl 0x55f86a0e12f0 <col:9, col:13> col:13 used a 'int'
| |-ParmVarDecl 0x55f86a0e1370 <col:16, col:20> col:20 used b 'int'
| `-CompoundStmt 0x55f86a0e15a8 <col:23, line:5:1>
|   `-ReturnStmt 0x55f86a0e1598 <line:4:3, col:14>
|     `-BinaryOperator 0x55f86a0e1578 <col:10, col:14> 'int' '+'
|       |-ImplicitCastExpr 0x55f86a0e1548 <col:10> 'int' <LValueToRValue>
|       | `-DeclRefExpr 0x55f86a0e1508 <col:10> 'int' lvalue ParmVar 0x55f86a0e12f0 'a' 'int'
|       `-ImplicitCastExpr 0x55f86a0e1560 <col:14> 'int' <LValueToRValue>
|         `-DeclRefExpr 0x55f86a0e1528 <col:14> 'int' lvalue ParmVar 0x55f86a0e1370 'b' 'int'
`-FunctionDecl 0x55f86a0e1608 <line:7:1, line:11:1> line:7:6 main 'void ()'
  `-CompoundStmt 0x55f86a0e1a20 <col:13, line:11:1>
    |-DeclStmt 0x55f86a0e1750 <line:8:3, col:12>
    | `-VarDecl 0x55f86a0e16c8 <col:3, col:11> col:7 used x 'int' cinit
    |   `-IntegerLiteral 0x55f86a0e1730 <col:11> 'int' 3
    |-DeclStmt 0x55f86a0e1808 <line:9:3, col:12>
    | `-VarDecl 0x55f86a0e1780 <col:3, col:11> col:7 used y 'int' cinit
    |   `-IntegerLiteral 0x55f86a0e17e8 <col:11> 'int' 4
    `-CallExpr 0x55f86a0e19c0 <line:10:3, col:27> 'int'
      |-ImplicitCastExpr 0x55f86a0e19a8 <col:3> 'int (*)(const char *, ...)' <FunctionToPointerDecay>
      | `-DeclRefExpr 0x55f86a0e1820 <col:3> 'int (const char *, ...)' Function 0x55f86a0c9450 'printf' 'int (const char *, ...)'
      |-ImplicitCastExpr 0x55f86a0e1a08 <col:10> 'const char *' <NoOp>
      | `-ImplicitCastExpr 0x55f86a0e19f0 <col:10> 'char *' <ArrayToPointerDecay>
      |   `-StringLiteral 0x55f86a0e1840 <col:10> 'char[4]' lvalue "%d\n"
      `-CallExpr 0x55f86a0e1900 <col:18, col:26> 'int'
        |-ImplicitCastExpr 0x55f86a0e18e8 <col:18> 'int (*)(int, int)' <FunctionToPointerDecay>
        | `-DeclRefExpr 0x55f86a0e1860 <col:18> 'int (int, int)' Function 0x55f86a0e1450 'add' 'int (int, int)'
        |-ImplicitCastExpr 0x55f86a0e1930 <col:22> 'int' <LValueToRValue>
        | `-DeclRefExpr 0x55f86a0e1880 <col:22> 'int' lvalue Var 0x55f86a0e16c8 'x' 'int'
        `-ImplicitCastExpr 0x55f86a0e1948 <col:25> 'int' <LValueToRValue>
          `-DeclRefExpr 0x55f86a0e18a0 <col:25> 'int' lvalue Var 0x55f86a0e1780 'y' 'int'
```
### Step 3: Configure directory paths

Before execution you must tell the framework where to read seeds and where to write results.
1. Open `test_case_generation/src/test/Mutate.java` and set the absolute paths for:
```
sourceDir  // folder that contains all seed programs
destDir    // folder that will receive every generated test program
```
2. Open `model_checker_test/TestCPAchecker/src/main/Main.java` and set
```
testDir = <destDir>; // matches the value above
```
Note : Each transformation method creates its own destDir. For a new method, add another path entry.

Directory layout inside any destDir:
```
|-- <seed-name>/
| | -- initial_program.c
| | -- initial_transformed.c
| | -- ...     # other variants
| | -- cpachecker-result.txt
| | -- cbmc-result.txt
| | -- seahorn-result.txt

```
Running the same model checker with different configurations appends additional result txt files.
Edit `test_case_generation/.../src/tools/TestSpecificTool.java` to customise commands or add new configurations.

### Step 4: Run the workflow
1. generate test cases
```
cd test_case_generation/src/test
javac Mutate.java && java Mutate   # creates destDir/… hierarchy
```

2. launch a model-checker workflow (example: CBMC)
```
cd ../../../model_checker_test/TestCBMC/src/main
javac Main.java && java Main
```
When a verification result diverges from the oracle, the pair `<test-case-path> : <tool-config>` is appended to model-checker-false.txt in the corresponding workflow directory. Inspect that file to reproduce and minimise the failing example before filing an upstream issue.

# Detected Bugs
We evaluate our approach on three mainstream model checkers—CPAchecker, CBMC, and SeaHorn—and successfully detect 48 unique bugs, 41 of which have been confirmed. The public issues we filed in the official repositories contain details that could reveal the authors’ identities, which would violate the double-blind review policy. To address this, we consolidated all issue records in the [bug_report](https://github.com/Elowen-jjw/MCT-draftlink/tree/main/bug_report) folder. 

In this folder, each file corresponds to one bug. The naming convention follows the format: Number_(ModelChecker)\_(BugCategory)\_(TransformationUsed).

We also distinguish the interaction process using different boxes: "Me" represents our submissions, while "Developer" represents the responses from the developers of the respective model checkers.






