package Condition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Filter.CopyFile;

import java.util.Map;
import processtimer.ProcessStatus;
import processtimer.ProcessWorker;

public class IfExecResult {
	public static int errorCnt = 0;
	public static String filename = "";
	
	public static boolean assertIf(String mutationDir, int mutationCnt, File file, List<String> initialList, Map<Integer, List<String>> modifyLineMap) throws IOException, InterruptedException {
		String filename = file.getName();
		File folder = new File(mutationDir);
		File assertIfFolder = new File(folder.getParent() + "/assertIf/" + filename.substring(0,filename.lastIndexOf(".c")));
		if(assertIfFolder.exists() == false) {
			assertIfFolder.mkdirs();
//			System.out.println("mkdir: " + assertIfFolder.getAbsolutePath());
		}
		File newFile = new File(assertIfFolder.getAbsolutePath() + "/" + filename.substring(0, filename.lastIndexOf(".c")) + "_"+ mutationCnt+  ".c");
		if(newFile.exists()) {
			newFile.delete();
		}
		newFile.createNewFile();
		
		FileWriter fw = new FileWriter(newFile, true);
		PrintWriter pw = new PrintWriter(fw);
		
		for(int i=0; i<initialList.size();i++) {
			if(i == 0) {
				pw.println("#include<assert.h>");
				continue ;
			}
			if(modifyLineMap.containsKey(i)) {
				modifyLineMap.get(i).forEach( s -> {
					pw.println(s);
				});
				continue ;
			}
			pw.println(initialList.get(i));
		}
		
		pw.flush();
		fw.flush();
		pw.close();
		fw.close();
		
		//终端运行c文件
		File cmpBefore = new File(assertIfFolder.getAbsolutePath() + "/a.out");
    	if(cmpBefore.exists()) cmpBefore.delete();
    	
    	String commandCompile = "cd " + newFile.getParent() + " && gcc -g " + newFile.getName() + " -lm";
    	execTerminal(commandCompile);
    	
    	File cmp = new File(assertIfFolder.getAbsolutePath() + "/a.out");
    	if(cmp.exists() == false) {
    		for(File child: assertIfFolder.listFiles()) {
//    			child.delete();
    		}
//    		assertIfFolder.delete();
    		System.out.println(file.getName() + " error !");
    		CopyFile.copyFile(file.getAbsolutePath(), folder.getParent() + "/errorIfExec" );
    		writeErrorFile(file.getName(), folder.getParent(), initialList, modifyLineMap);
    		return false;
    	}
    	
    	String commandRun = "cd " + assertIfFolder.getAbsolutePath() + " && ./a.out";
    	List<String> execLines = execTerminal(commandRun);    	
    	
    	if(execLines.size() == 1 && execLines.get(0).trim().equals("timeout")) {
    		System.out.println(file.getName() + " timeout !");
    		if(cmp.exists()) cmp.delete();
    		for(File child: assertIfFolder.listFiles()) {
    			child.delete();
    		}
//    		assertIfFolder.delete();
    		return false;
    	}
		for(String s: execLines) {
			if(s.matches(".*Assertion.*failed.*")) {
				cmp.delete();
				for(File child: assertIfFolder.listFiles()) {
//	    			child.delete();
	    		}
//	    		assertIfFolder.delete();
	    		return false;
			}
		}
		
		cmp.delete();
		for(File child: assertIfFolder.listFiles()) {
//			child.delete();
		}
//		assertIfFolder.delete();
		return true;
	}
	
	public static void writeErrorFile(String srcFileName, String mutationDir, List<String> initialList, Map<Integer, List<String>> modifyLineMap) throws IOException {
		File errorFolder = new File(mutationDir + "/errorAssertIf/" + srcFileName.substring(0,srcFileName.lastIndexOf(".c")));
		if(errorFolder.exists() == false) {
			errorFolder.mkdirs();
		}
//		System.out.println("mkdir: " + errorFolder.getAbsolutePath());
		if(!srcFileName.equals(filename)) {
			filename = srcFileName;
			errorCnt = 0;
		}
		File errorFile = new File(errorFolder.getAbsolutePath() + "/" + filename.substring(0, filename.lastIndexOf(".c")) + "_"+ errorCnt+  ".c");
		errorCnt++;
		if(errorFile.exists()) {
			errorFile.delete();
		}
		errorFile.createNewFile();
		
		FileWriter fw = new FileWriter(errorFile, true);
		PrintWriter pw = new PrintWriter(fw);
		
		for(int i=0; i<initialList.size();i++) {
			if(i == 0) {
				pw.println("#include<assert.h>");
				continue ;
			}
			if(modifyLineMap.containsKey(i)) {
				modifyLineMap.get(i).forEach( s -> {
					pw.println(s);
				});
				continue ;
			}
			pw.println(initialList.get(i));
		}
		
		pw.flush();
		fw.flush();
		pw.close();
		fw.close();
	}
	
	public static List<String> execTerminal(String command) throws IOException, InterruptedException{
		
		String[] cmd = new String[] { "/bin/bash", "-c", command };
		ProcessBuilder builder = new ProcessBuilder(cmd);
		builder.redirectErrorStream(true);
		Process proc = builder.start();
		
		ProcessWorker pw = new ProcessWorker(proc);
		pw.start();
		ProcessStatus ps = pw.getPs();
		try {
			pw.join(8 * 1000);
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
