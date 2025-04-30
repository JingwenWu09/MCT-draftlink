package processtimer;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.util.ArrayList;

public class CheckMemory implements Runnable{
    Process process;
    public CheckMemory(Process process){
        this.process = process;
    }

    public CheckMemory(){}
    @Override
    public void run() {
        while(process.isAlive()){
            try {
                Sigar sigar = new Sigar();

//                for(long pid: sigar.getProcList()){
//                    ProcState ps = sigar.getProcState(pid);
//                    System.out.println("pid:" + pid + " name:" + ps.getName());
//                }
                Mem mem = sigar.getMem();
                // 内存总量

                System.out.println("pid: " + process.pid() +" ——内存实际占用情况： " + mem.getActualUsed() / 1024 / 1024 /1024  + "g");
                System.out.println("pid: " + process.pid() + " ——内存实际空闲情况： " + mem.getActualFree() / 1024 / 1024 /1024  + "g");
    //            if(mem.getActualFree() < (mem.getTotal() * 0.15)){
    //                ProcessTerminal.result = new ArrayList<>();
    //                process.destroy();
    //            }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
//                e.printStackTrace();
            } catch (SigarException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
