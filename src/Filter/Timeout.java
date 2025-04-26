package Filter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import processtimer.ProcessStatus;
import processtimer.ProcessWorker;

public class Timeout {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		File folder = new File("/home/jing/Desktop/all");
		String destpath = "/home/jing/Desktop/timeout";
		Timeout.serachTimeout(folder, destpath);
	}
	
	public static void serachTimeout(File folder, String destpath) throws IOException, InterruptedException {
		File[] fileList = folder.listFiles();
        
		int i = 0;
        for (File file : fileList) {
        	String filename = file.getName().trim();
        	if(filename.endsWith(".c") == false ) continue ;
        	
        	System.out.println("i = " + i++ + ", " + filename);
        	
        	File cmpBefore = new File(file.getParent() + "/a.out");
        	if(cmpBefore.exists()) cmpBefore.delete();
        	
        	String commandCompile = "cd " + file.getParent() + " && gcc -g " + filename + " -lm";
        	testProcessThread(commandCompile);
        	
        	File cmp = new File(file.getParent() + "/a.out");
        	if(cmp.exists() == false) {
        		System.out.println("error");
        		file.delete();
        		continue ;
        	}
        	
        	String commandRun = "cd " + file.getParent() + " && ./a.out";
        	List<String> execRunLines = testProcessThread(commandRun);
        	
        	if(execRunLines.size() == 1 && execRunLines.get(0).trim().equals("timeout")) {
        		CopyFile.copyFile(file.getAbsolutePath(), destpath);
        		System.out.println("timeout:" + file.getAbsolutePath());
        	}
        	
        	File cmpAfter = new File(file.getParent() + "/a.out");
        	if(cmpAfter.exists()) {
        		cmpAfter.delete();
        	}
        	file.delete();
        }
	}
        
    public static List<String> testProcessThread(String command) throws IOException, InterruptedException{
		
		String[] cmd = new String[] { "/bin/bash", "-c", command };
		ProcessBuilder builder = new ProcessBuilder(cmd);
		builder.redirectErrorStream(true);
		Process proc = builder.start();
		
		ProcessWorker pw = new ProcessWorker(proc);
		pw.start();
		ProcessStatus ps = pw.getPs();
		try {
			pw.join(5 * 1000);
			if(ps.exitCode == ps.CODE_STARTED) {
				pw.interrupt();
				List<String> result = new ArrayList<String>();
				result.add("timeout");
				return result;
			}
			else {
				return ps.output;
			}
		}catch(InterruptedException e) {
			pw.interrupt();
			throw e;
		}finally {
			proc.destroy();
		}
	}

}
