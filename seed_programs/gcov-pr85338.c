#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void Test(long long Val, int Amt) {
  __builtin_printf("  lshr: 0x%llx \t\t shl: 0x%llx\n", Val >> Amt, Val << Amt);
  __builtin_printf("  lshr: 0x%llx\t\tshl: 0x%llx\n", Val >> Amt, Val << Amt);
  __builtin_printf("  lshr: 0x%llx \t\t shl: 0x%llx\n", (unsigned long long)Val >> Amt, Val << Amt);
}

int main() {
  Test(10, 4);

  return 0;
}
