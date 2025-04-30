package processtimer;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessTerminal {

	public List<String> testProcessThread(String command, int second) throws IOException, InterruptedException{
		
		String[] cmd = new String[] { "/bin/bash", "-c", command };
		ProcessBuilder builder = new ProcessBuilder(cmd);
		builder.redirectErrorStream(true);
		Process proc = builder.start();
		
		ProcessWorker pw = new ProcessWorker(proc);
		pw.start();
		ProcessStatus ps = pw.getPs();
		try {
			pw.join(second * 1000);
			if(ps.exitCode == ps.CODE_STARTED) {
				pw.interrupt();
				List<String> result = new ArrayList<String>();
				proc.destroy();
				return result;
			}
			else {
				proc.destroy();
				return ps.output;
			}
		}catch(InterruptedException e) {
			pw.interrupt();
			proc.destroy();
			throw e;
		}
	}
	
public List<String> testProcessThreadNotLimit(String command) throws IOException, InterruptedException{
		
		String[] cmd = new String[] { "/bin/bash", "-c", command };
		ProcessBuilder builder = new ProcessBuilder(cmd);
		builder.redirectErrorStream(true);
		Process proc = builder.start();
		
		ProcessWorker pw = new ProcessWorker(proc);
		pw.start();
		ProcessStatus ps = pw.getPs();
		try {
			pw.join();
			if(ps.exitCode == ps.CODE_STARTED) {
				pw.interrupt();
				List<String> result = new ArrayList<String>();
				proc.destroy();
				return result;
			}
			else {
				proc.destroy();
				return ps.output;
			}
		}catch(InterruptedException e) {
			pw.interrupt();
			proc.destroy();
			throw e;
		}
	}
	
public void testProcessThreadNotTimeLimit(String command) throws IOException, InterruptedException{
		
		String[] cmd = new String[] { "/bin/bash", "-c", command };
		ProcessBuilder builder = new ProcessBuilder(cmd);
		builder.redirectErrorStream(true);
		Process proc = builder.start();
		
		ProcessWorker pw = new ProcessWorker(proc);
		pw.start();
		ProcessStatus ps = pw.getPs();
		try {
			pw.join();
			if(ps.exitCode == ps.CODE_STARTED) {
				pw.interrupt();
			}
		}catch(InterruptedException e) {
			pw.interrupt();
			throw e;
		}finally {
			proc.destroy();
		}
	}

	public List<String> execTerminal(String command, int second){
		try {
			List<String> resultLines = new ArrayList<String>();
			String[] cmd = new String[] { "/bin/bash", "-c", command };
			String execLine = "";
			ProcessBuilder builder = new ProcessBuilder(cmd);
			builder.redirectErrorStream(true);
			Process proc = builder.start();

			if (!proc.waitFor(second, TimeUnit.SECONDS)) {
				//proc.destroy();
				return new ArrayList<String>();
			}

			InputStream ins = proc.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
			while((execLine = br.readLine()) != null) {
				resultLines.add(execLine);
			}

			//proc.destroy();

			return resultLines;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}


	public void killSeahornThread(){
		try {
			Sigar sigar = new Sigar();

			for (long pid : sigar.getProcList()) {
				ProcState ps = sigar.getProcState(pid);
				if(ps.getName().startsWith("sea")){
					System.out.println(pid + " will be killed.............");
					Runtime.getRuntime().exec("kill -9 " + pid);
				}
			}

		} catch (SigarException e) {
			killSeahornThread();
		} catch (IOException e) {

		}
	}

	public int checkSeahornCount(){
		int count = 0;
		try {
			Sigar sigar = new Sigar();

			for (long pid : sigar.getProcList()) {
				ProcState ps = sigar.getProcState(pid);
				if(ps.getName().startsWith("sea")){
					count++;
				}
			}
		} catch (SigarException e) {
			throw new RuntimeException(e);
		}
		return count;
	}

	public void printMemInfo(){
		try {
			Sigar sigar = new Sigar();
			Mem mem = sigar.getMem();

			System.out.println("内存实际占用情况： " + mem.getActualUsed() / 1024 / 1024 /1024  + "g");
			System.out.println("内存实际空闲情况： " + mem.getActualFree() / 1024 / 1024 /1024  + "g");
		}  catch (SigarException e) {
			throw new RuntimeException(e);
		}
	}
	
}
