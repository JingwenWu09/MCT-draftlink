# Bug #1 in SeaHorn was fixed as a third-party dependency related issue. It was exposed by a test case generated using probability-guided branch reordering transformation.

```
Original mutate (not reduced):
#include <assert.h>

int baz3(int i) {
  int A[1];
  int B[1];
  for( int i = 0; i < 1; i++){ 
    A[i] = i;
    B[i] = i;
  }
  for(int i = 0; i < 1; i++) {
    sassert(A[i] == B[i]) ;
  }
  return A[0];
}

int main() {
  int c = 0x2180;
  char g = '\n';
  int skip = 0;

  if (!((c & 0x10) && (c & 0x4000)) && (c & 2)) skip = 1;
  if ((c & 0x2000)) skip = 1;
  if ((c & 0x1408)) skip = 1;
  if ((c & 0x200))  skip = 1;

  if (skip) return 0;

  if (c & 0x80) {
    for (int i = 0; i < 5; i++) {
      if (baz3(i)) break;
    }
  }

  // PGBR
  skip = 0;
  if ((c & 0x2000)) skip = 1;
  else if ((c & 0x1408)) skip = 1;
  else if ((c & 0x200)) skip = 1;
  else if (!((c & 0x10) && (c & 0x4000)) && (c & 2)) skip = 1;

  if (skip) return 0;

  if (c & 0x80) {
    for (int i = 0; i < 5; i++) {
      if (baz3(i)) break;
    }
  }

  return 0;
}
```
```
//after reducing (the simplest version that triggers this bug)
Me: 
#include  "seahorn/seahorn.h"
int main() {
  int A[1];
  int B[1];
  for( int i = 0; i < 1; i++){ 
    A[i] = i;
    B[i] = i;
  }
  for(int i = 0; i < 1; i++) {
    sassert(A[i] == B[i]) ;
  }
  return 0;
}

For example.c, I run sea bpf -m64 example.c, and SeaHorn did not give sat or unsat results, but instead displays the following information:
/usr/bin/clang+llvm-14.0.0/bin/clang-14 -c -emit-llvm -D__SEAHORN__ -fdeclspec -O1 -Xclang -disable-llvm-optzns -fgnu89-inline -m64 -I/home/jing/seahorn/build/run/include -o /tmp/sea-e9rem34k/demo1.bc demo1.c
/home/jing/seahorn/build/run/bin/seapp -o /tmp/sea-e9rem34k/demo1.pp.bc --simplifycfg-sink-common=false --strip-extern=false --promote-assumptions=false --kill-vaarg=true --ignore-def-verifier-fn=false --horn-keep-arith-overflow=false /tmp/sea-e9rem34k/demo1.bc
/home/jing/seahorn/build/run/bin/seapp --simplifycfg-sink-common=false -o /tmp/sea-e9rem34k/demo1.pp.ms.bc --horn-mixed-sem --ms-reduce-main /tmp/sea-e9rem34k/demo1.pp.bc
/home/jing/seahorn/build/run/bin/seaopt -f --simplifycfg-sink-common=false -o /tmp/sea-e9rem34k/demo1.pp.ms.o.bc -O3 --seaopt-enable-indvar=false --seaopt-enable-loop-idiom=false --unroll-threshold=150 --unroll-allow-partial=false --unroll-partial-threshold=0 --vectorize-slp=false /tmp/sea-e9rem34k/demo1.pp.ms.bc
/home/jing/seahorn/build/run/bin/seaopt -f -o /tmp/sea-e9rem34k/demo1.pp.ms.o.ul.bc -loop-simplify -fake-latch-exit -sea-loop-unroll /tmp/sea-e9rem34k/demo1.pp.ms.o.bc
/home/jing/seahorn/build/run/bin/seapp -o /tmp/sea-e9rem34k/demo1.pp.ms.o.ul.cut.bc --horn-cut-loops --back-edge-cutter-with-asserts=false /tmp/sea-e9rem34k/demo1.pp.ms.o.ul.bc
 #0 0x000055566bccbc03 llvm::sys::PrintStackTrace(llvm::raw_ostream&, int) (/home/jing/seahorn/build/run/bin/seapp+0x1bddc03)
 #1 0x000055566bcc9c9c llvm::sys::RunSignalHandlers() (/home/jing/seahorn/build/run/bin/seapp+0x1bdbc9c)
 #2 0x000055566bccc08f SignalHandler(int) Signals.cpp:0:0
 #3 0x00007f9215a42520 (/lib/x86_64-linux-gnu/libc.so.6+0x42520)
 #4 0x000055566aba1849 seahorn::markAncestorBlocks(llvm::ArrayRef<llvm::BasicBlock const*>, llvm::DenseSet<llvm::BasicBlock const*, llvm::DenseMapInfo<llvm::BasicBlock const*, void> >&) (/home/jing/seahorn/build/run/bin/seapp+0xab3849)
 #5 0x000055566aba2cd6 seahorn::reduceToAncestors(llvm::Function&, llvm::ArrayRef<llvm::BasicBlock const*>, seahorn::SeaBuiltinsInfo&) (/home/jing/seahorn/build/run/bin/seapp+0xab4cd6)
 #6 0x000055566aba2dff seahorn::reduceToReturnPaths(llvm::Function&, seahorn::SeaBuiltinsInfo&) (/home/jing/seahorn/build/run/bin/seapp+0xab4dff)
 #7 0x000055566ab9b268 (anonymous namespace)::BackedgeCutter::runOnFunction(llvm::Function&) BackedgeCutter.cc:0:0
 #8 0x000055566a9a274d llvm::FPPassManager::runOnFunction(llvm::Function&) (/home/jing/seahorn/build/run/bin/seapp+0x8b474d)
 #9 0x000055566a9a94f3 llvm::FPPassManager::runOnModule(llvm::Module&) (/home/jing/seahorn/build/run/bin/seapp+0x8bb4f3)
#10 0x000055566a9a3270 llvm::legacy::PassManagerImpl::run(llvm::Module&) (/home/jing/seahorn/build/run/bin/seapp+0x8b5270)
#11 0x000055566a55566e main (/home/jing/seahorn/build/run/bin/seapp+0x46766e)
#12 0x00007f9215a29d90 (/lib/x86_64-linux-gnu/libc.so.6+0x29d90)
#13 0x00007f9215a29e40 __libc_start_main (/lib/x86_64-linux-gnu/libc.so.6+0x29e40)
#14 0x000055566a5521a5 _start (/home/jing/seahorn/build/run/bin/seapp+0x4641a5)
PLEASE submit a bug report to https://github.com/llvm/llvm-project/issues/ and include the crash backtrace.
Stack dump:
0.	Program arguments: /home/jing/seahorn/build/run/bin/seapp -o /tmp/sea-e9rem34k/demo1.pp.ms.o.ul.cut.bc --horn-cut-loops --back-edge-cutter-with-asserts=false /tmp/sea-e9rem34k/demo1.pp.ms.o.ul.bc
1.	Running pass 'Function Pass Manager' on module '/tmp/sea-e9rem34k/demo1.pp.ms.o.ul.bc'.
2.	Running pass 'BackedgeCutter' on function '@main'

Then, I made two changes on example.c and they produced the following results:

I changed the command to use bf instead of bpf, and the result was unsat.
I changed the number of array elements (e.g. int A[2]; int B[2];, etc.), the result was unsat.

I have some questions:
When the number of array element is 1, why does bpf generate the information about crash backtrace, while bf can give the unsat reslult? What's the difference between these two instructions?
bpf can only give unsat results if the number of arrays is greater than 1. So what effects does array size have on bpf?
Is there any way to avoid bpf generating crash backtrace message when dealing with arrays with one element ?
Please help me to explain the situations. Thanks.

Version: seahorn 14.0.0
OS: ubuntu 22.04

```

```
Developer:

This is just a bug. I'll mark this issue as such. Fixed by commit 7206584.
```




