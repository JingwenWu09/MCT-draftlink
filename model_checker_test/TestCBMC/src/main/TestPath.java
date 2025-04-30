package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import processtimer.ProcessTerminal;
public class TestPath{
	public static void main(String args[]) throws IOException, InterruptedException{
		String filename = "20000801-1_0.c";
		String command = "cd /Users/elowen/Desktop && cbmc " + filename + " --bounds-check --pointer-check --memory-leak-check --div-by-zero-check --signed-overflow-check --unsigned-overflow-check --pointer-overflow-check --conversion-check --undefined-shift-check --pointer-primitive-check";
		String[] cmd = new String[] { "/bin/bash", "-c", command };

		List<String> execResult = new ArrayList<String>();
		String execLine = "";
		
		ProcessBuilder builder = new ProcessBuilder(cmd);
		builder.redirectErrorStream(true);
		Process proc = builder.start();
		if (!proc.waitFor(8, TimeUnit.SECONDS)) {
		    proc.destroy();
		    return;
		}
		
		InputStream ins = proc.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
		
		
		while((execLine = br.readLine()) != null) {
			execResult.add(execLine);
		}
		if(execResult.get(execResult.size()-1).contains("FAILURE")) {
			ProcessTerminal pt = new ProcessTerminal();
			pt.testProcessThreadNotTimeLimit("cd /Users/elowen/Desktop && cp " + filename + " ./error");
		}
		System.out.println("end!");
	}
	
}