#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);

#if defined(__alpha__)

#elif defined(__arc__)
#define PIC_REG "26"
#elif defined(__arm__)
#define PIC_REG "9"
#elif defined(AVR)

#elif defined(__cris__)
#define PIC_REG "0"
#elif defined(__epiphany__)
#define PIC_REG "r28"
#elif defined(__fr30__)

#elif defined(__H8300__) || defined(__H8300H__) || defined(__H8300S__)

#elif defined(_IBMR2)

#elif defined(__i386__)
#define PIC_REG "ebx"
#elif defined(__ia64__)

#elif defined(__lm32__)

#elif defined(__loongarch__)

#elif defined(__M32R__)

#elif defined(__m68k__)
#define PIC_REG "a5"
#elif defined(__mc68hc1x__)

#elif defined(__mcore__)

#elif defined(__MICROBLAZE__)
#define PIC_REG "r20"
#elif defined(__mips__)

#elif defined(__MMIX__)

#elif defined(__mn10300__)

#elif defined(__moxie__)

#elif defined(__nds32__)

#elif defined(__nios2__)

#elif defined(__hppa__)

#elif defined(__pdp11__)

#elif defined(__powerpc__) || defined(__PPC__) || defined(__POWERPC__) || defined(__ppc)
#ifdef __MACH__
#define PIC_REG "31"
#else
#define PIC_REG "30"
#endif
#elif defined(__riscv)

#elif defined(__RX__)

#elif defined(__s390__)
#define PIC_REG "12"
#elif defined(__sparc__)
#define PIC_REG "l7"
#elif defined(__tile__)
#define PIC_REG "r51"
#elif defined(__TMS320C6X__)
#define PIC_REG "B14"
#elif defined(__v850)

#elif defined(__vax__)

#elif defined(__VISIUM__)

#elif defined(__xstormy16__)

#elif defined(__XTENSA__)

#elif defined(__sh__)
#define PIC_REG "r12"
#elif defined(__x86_64__)

#elif defined(__m32c__)

#elif defined(__frv__)
#ifdef __FRV_FDPIC__
#define PIC_REG "gr15"
#else
#define PIC_REG "gr17"
#endif
#elif defined(__aarch64__)

#elif defined(__RL78__)

#elif defined(__MSP430__)

#elif defined(__nvptx__)

#elif defined(__csky__)

#if defined(__CK807__) || defined(__CK810__)
#define PIC_REG "r28"
#endif
#elif defined(__or1k__)

#elif defined(__AMDGCN__)

#elif defined(__PRU__)

#else
#error "Modify the test for your target."
#endif

#if defined PIC_REG && !defined __PIC__ && !defined __pic__
register void *reg __asm__(PIC_REG);
#else

static void *reg;
#endif

void *__attribute__((noinline)) dummy(void *x) {
  return x;
}

void f(void) {
  goto *dummy(&&bar);
  for (;;) {
  foo:
    reg = (void *)1;
    if (!reg) {
      goto baz;
    }
    reg = &&foo;
  }

bar:
baz:
  reg = 0;
}

int main() {
  void *old_reg = reg;
  reg = (void *)1;

  f();

#if !defined(__sparc__) && !defined(__MACH__)
  if (reg) {
    abort();
  }
#endif

  reg = old_reg;
  return 0;
}
