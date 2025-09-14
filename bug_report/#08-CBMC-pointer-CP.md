# Bug #8 in CBMC was unconfirmed as a pointer alias related issue. It was exposed by a test case generated using constant propagation transformation.

```
Me:

// Example 1:
#include <stdlib.h>
#include <assert.h>
typedef __CHAR16_TYPE__ char16_t;
#define A  0x0061
#define Y1 0xD950
#define Y2 0xDF21
char16_t s3_explicit[] = {A, Y1, Y2, 0x0000};
int main() {
    char16_t v0 = s3_explicit[0];
    char16_t v1 = s3_explicit[1];
    char16_t v2 = s3_explicit[2];
    char16_t v3 = s3_explicit[3];
    assert(v0 == A);
    assert(v1 == Y1);
    assert(v2 == Y2);
    assert(v3 == 0x0000);
    return 0;
}

// Example 2:
#include <stdlib.h>
#include <assert.h>
typedef __CHAR16_TYPE__ char16_t;
#define A  0x0061      // UTF-16 code unit for 'a'
#define Y1 0xD950      // High surrogate for U+64321
#define Y2 0xDF21      // Low surrogate for U+64321
char16_t *s5_orig = u"a" "\U00064321";
char16_t s5_explicit[] = {A, Y1, Y2, 0x0000};
int main() {
    char16_t o0 = s5_orig[0];
    char16_t o1 = s5_orig[1];
    char16_t o2 = s5_orig[2];
    char16_t o3 = s5_orig[3];
    char16_t v0 = s5_explicit[0];
    char16_t v1 = s5_explicit[1];
    char16_t v2 = s5_explicit[2];
    char16_t v3 = s5_explicit[3];
    assert(o0 == v0); // R(o0) = v0
    assert(o1 == v1); // R(o1) = v1
    assert(o2 == v2); // R(o2) = v2
    assert(o3 == v3); // R(o3) = v3
    return 0;
}

// Example 3:
#include <stdlib.h>
#include <assert.h>
typedef __CHAR32_TYPE__ char32_t;
#define A  0x00000061     // 'a'
#define Y  0x00064321     // full Unicode code point
char32_t *s5_orig = U"a" "\U00064321";
char32_t s5_explicit[] = {A, Y, 0x00000000};
int main() {
    char32_t o0 = s5_orig[0];
    char32_t o1 = s5_orig[1];
    char32_t o2 = s5_orig[2];
    char32_t v0 = s5_explicit[0];
    char32_t v1 = s5_explicit[1];
    char32_t v2 = s5_explicit[2];
    assert(o0 == v0); // R(o0) = v0
    assert(o1 == v1); // R(o1) = v1
    assert(o2 == v2); // R(o2) = v2
    return 0;
}

In these three examples, cbmc gives the FAILURE result in all assertions. 

CBMC version: 5.88.0
Operating system: Ubuntu 22.04, MacOS
command line: cbmc example.c

```


