package processtimer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ProcessWorker extends Thread{
	private Process process;
	private ProcessStatus ps;

	public ProcessWorker(Process process) {
		this.process = process;
		this.ps = new ProcessStatus();
	}
	
	public void run() {
		try {
			InputStream ins = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
			String line = "";
			List<String> outputs = new ArrayList<String>();
			ps.exitCode = ps.CODE_STARTED;
			while((line = br.readLine()) != null) {
//				System.out.println(line);
				outputs.add(line);
			}
			ps.output = outputs;
			ps.exitCode = process.waitFor();
		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	
	public Process getProcess() {
		return process;
	}
	public void setProcess(Process process) {
		this.process = process;
	}
	public ProcessStatus getPs() {
		return ps;
	}
	public void setPs(ProcessStatus ps) {
		this.ps = ps;
	}
	
}
