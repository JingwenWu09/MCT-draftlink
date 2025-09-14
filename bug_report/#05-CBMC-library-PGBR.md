# Bug #5 in CBMC was confirmed as a C standard library related issue. It was exposed by a test case generated using probability-guided branch reordering transformation. 

```
Me:

#include<assert.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
    FILE *f = fopen("fred.txt", "w");
    fwrite("hello\nhello\n", 1, 12, f);
    fclose(f);

    char freddy[7];
    f = fopen("fred.txt", "r");
    if (fread(freddy, 1, 6, f) != 6) {
      printf("couldn't read fred.txt\n");
    }

    freddy[6] = '\0';
    fclose(f);
    
    printf("%s", freddy);

    int InChar;
    char ShowChar;
    f = fopen("fred.txt", "r");

    int InChar_1 = InChar;
  
    char ShowChar_1 = ShowChar;
    while ((InChar = fgetc(f)) != EOF) {
        ShowChar = InChar;
        if (ShowChar < ' ') {
            ShowChar = '.';
        }
        printf("ch: %d '%c'\n", InChar, ShowChar);
    }
    fclose(f);
    
    f = fopen("fred.txt", "r");
    if ((InChar_1 = fgetc(f)) != EOF){
      do {
        ShowChar_1 = InChar_1;
        if (ShowChar_1 < ' ') {
          ShowChar_1 = '.';
        }
        printf("ch: %d '%c'\n", InChar_1, ShowChar_1);
      }while ((InChar_1 = fgetc(f)) != EOF);
    }
    fclose(f);

    assert(InChar == InChar_1);
    assert(ShowChar == ShowChar_1);

    return 0;
}

In this example, the assert() functions are both true, which is confirmed by compiling with gcc and clang,
while cbmc gives the FAILURE result in assertion ShowChar == ShowChar_1, which means the assertion is false.
Please help me to explain why it happens, thanks.

CBMC version: 5.88.0
Operating system: Ubuntu 22.04, macOS
Command line: cbmc example.c
```
```
Developer:

CBMC ships an approximating model of (some) C library functions. Specifically, CBMC treats all file I/O as producing non-deterministic values.
If you require a more precise model of these functions (fgetc, and perhaps also fopen and fread)
then please add these implementations to your verification harness.
```



