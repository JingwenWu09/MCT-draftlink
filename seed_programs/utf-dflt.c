#include <math.h>
#include <stdio.h>
#include <stdlib.h>

const unsigned short c0 = u'a';

const unsigned long c1 = U'a';

#define u 1 +
#define U 2 +

const unsigned short c2 = u'a';
const unsigned long c3 = U'a';

#undef u
#undef U
#define u "a"
#define U "b"

const void *s0 = u"a";
const void *s1 = U"a";

int main() {
}
