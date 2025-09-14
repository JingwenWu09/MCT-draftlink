#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct {
  __CHAR16_TYPE__ s[2];
} a[] = {u"ff", [0].s[0] = u'x', [1] = u"\u1234\u4567", [1].s[0] = u'\u89ab'};

int main() {
  if (a[0].s[0] != u'x' || a[0].s[1] != u'f' || a[1].s[0] != u'\u89ab' || a[1].s[1] != u'\u4567') {
    __builtin_abort();
  }
  return 0;
}
