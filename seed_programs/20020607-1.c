#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct cpp_num {
  long high;
  long low;
  char overflow;
};

#define num_eq(num1, num2) (num1.low == num2.low && num1.high == num2.high)

static struct cpp_num num_equality_op(struct cpp_num lhs, struct cpp_num rhs) {
  lhs.low = num_eq(lhs, rhs);
  lhs.high = 0;
  lhs.overflow = 0;
  return lhs;
}

int main() {
  struct cpp_num a = {1, 2};
  struct cpp_num b = {3, 4};

  struct cpp_num result = num_equality_op(a, b);
  if (result.low) {
    return 1;
  }

  result = num_equality_op(a, a);
  if (!result.low) {
    return 2;
  }

  return 0;
}
