#include <math.h>
#include <stdio.h>
#include <stdlib.h>

char *buffer1;
char *buffer2;

#define SIZE 1000

int main(void) {
  const char *const foo1 = "hello world";

  buffer1 = __builtin_malloc(SIZE);
  __builtin_strcpy(buffer1, foo1);
  buffer2 = __builtin_malloc(SIZE);
  __builtin_strcpy(buffer2, foo1);

  if (__builtin_memchr("hello world", 'x', 11))
    __builtin_abort();
  if (__builtin_memchr("hello world", 'x', 0) != 0)
    __builtin_abort();
  if (__builtin_memchr("hello world", 'w', 2))
    __builtin_abort();
  if (__builtin_memchr("hello world", 'd', 10))
    __builtin_abort();
  if (__builtin_memchr("hello world", '\0', 11))
    __builtin_abort();

  if (__builtin_strcmp("hello", "aaaaa") <= 0)
    __builtin_abort();
  if (__builtin_strcmp("aaaaa", "aaaaa") != 0)
    __builtin_abort();
  if (__builtin_strcmp("aaaaa", "") <= 0)
    __builtin_abort();
  if (__builtin_strcmp("", "aaaaa") >= 0)
    __builtin_abort();
  if (__builtin_strcmp("ab", "ba") >= 0)
    __builtin_abort();

  if (__builtin_strncmp("hello", "aaaaa", 0) != 0)
    __builtin_abort();
  if (__builtin_strncmp("aaaaa", "aaaaa", 100) != 0)
    __builtin_abort();
  if (__builtin_strncmp("aaaaa", "", 100) <= 0)
    __builtin_abort();
  if (__builtin_strncmp("", "aaaaa", 100) >= 0)
    __builtin_abort();
  if (__builtin_strncmp("ab", "ba", 1) >= 0)
    __builtin_abort();
  if (__builtin_strncmp("aab", "aac", 2) != 0)
    __builtin_abort();

  if (__builtin_strcasecmp("aaaaa", "aaaaa") != 0)
    __builtin_abort();

  if (__builtin_strncasecmp("hello", "aaaaa", 0) != 0)
    __builtin_abort();
  if (__builtin_strncasecmp("aaaaa", "aaaaa", 100) != 0)
    __builtin_abort();
  if (__builtin_strncasecmp("aab", "aac", 2) != 0)
    __builtin_abort();

  if (__builtin_memcmp("aaaaa", "aaaaa", 6) != 0)
    __builtin_abort();

  return 0;
}
