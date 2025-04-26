package AssrtFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Filter.CopyFile;

import java.io.File;
import Filter.CopyFile;

import processtimer.ProcessStatus;
import processtimer.ProcessWorker;

public class RunAssert {
	
	public static long assertMutation(File folder, long successCnt, File mutationFolder) throws IOException, InterruptedException {
		if(folder.exists()==false) {
			System.out.println("folder not exist!");
			return 0;
		}
		File[] fileList = folder.listFiles();
		
        assert fileList != null;
        for (File file : fileList) {
            if (file.isDirectory()) {
//            	if(file.getName().equals("pr45636") == false) continue ;
            	if(file.getName().equals("error")) continue ;
            	if(file.getName().equals("timeout")) continue ;
            	if(file.getName().equals("assertFailed")) continue ;
            	if(file.getName().equals("success")) continue ;
            	if(file.getName().equals("assertIf")) continue ;
            	if(file.getName().equals("errorIfExec")) continue ;
            	if(file.getName().equals("errorAssertIf")) continue ;
            	successCnt = assertMutation(file, successCnt, mutationFolder);
            	if(file.listFiles().length == 0) file.delete();
            } else {
            	String filename = file.getName().trim();
            	if(filename.endsWith(".c") == false ) continue ;
            	
            	File parentFolder = new File(file.getParent());
            	String errorFolder = mutationFolder.getAbsolutePath() + "/error";
            	String timeoutFolder = mutationFolder.getAbsolutePath() + "/timeout";
            	String assertFailedFolder = mutationFolder.getAbsolutePath() + "/assertFailed";
            	String successFolder = mutationFolder.getAbsolutePath() + "/success";
            	
            	File cmp = new File(file.getParent() + "/a.out");
            	if(cmp.exists()) cmp.delete();
            	
            	String commandCompile = "cd " + file.getParent() + " && gcc -g " + filename + " -lm";
//            	testProcessThread(commandCompile);
            	
            	List<String> execCompileLines = testProcessThread(commandCompile);
            	if(execCompileLines.size() == 1 && execCompileLines.get(0).trim().equals("timeout")) {
            		File timeoutSubFolder = new File(timeoutFolder + "/" + parentFolder.getName());
            		if(timeoutSubFolder.exists() == false ) timeoutSubFolder.mkdirs();
            		CopyFile.copyFile(file.getAbsolutePath(), timeoutSubFolder.getAbsolutePath());
            		file.delete();
            		if(cmp.exists()) cmp.delete();
            		System.out.println("timeout:" + file.getAbsolutePath());
            		continue ;
            	}
            	
            	File cmpOut = new File(file.getParent() + "/a.out");
            	if(cmpOut.exists() == false) {
            		File errorSubFolder = new File(errorFolder + "/" + parentFolder.getName());
            		if(errorSubFolder.exists() == false ) errorSubFolder.mkdirs();
            		CopyFile.copyFile(file.getAbsolutePath(), errorSubFolder.getAbsolutePath());
            		file.delete();
            		
            		System.out.println("error:" + file.getAbsolutePath());
            		continue ;
            	}
            	
            	String commandRun = "cd " + file.getParent() + " && ./a.out";
            	List<String> execRunLines = testProcessThread(commandRun);
            	
            	boolean run_flag = true;
            	if(execRunLines.size() == 1 && execRunLines.get(0).trim().equals("timeout")) {
            		File timeoutSubFolder = new File(timeoutFolder + "/" + parentFolder.getName());
            		if(timeoutSubFolder.exists() == false ) timeoutSubFolder.mkdirs();
            		CopyFile.copyFile(file.getAbsolutePath(), timeoutSubFolder.getAbsolutePath());
            		file.delete();
            		if(cmpOut.exists()) cmpOut.delete();
            		System.out.println("timeout:" + file.getAbsolutePath());
            		
            		continue ;
            	}
            	
            	for(String line :execRunLines) {
            		if(line.matches(".*Assertion.*failed.*")) {
            			File assertFailedSubFolder = new File(assertFailedFolder + "/" + parentFolder.getName());
                		if(assertFailedSubFolder.exists() == false ) assertFailedSubFolder.mkdirs();
            			CopyFile.copyFile(file.getAbsolutePath(), assertFailedSubFolder.getAbsolutePath());
            			file.delete();
                		if(cmpOut.exists()) cmpOut.delete();

                		run_flag = false;
                		System.out.println("failed:" + file.getAbsolutePath());
                		break ;
            		}
            	}
            	
            	if( run_flag == true) {
            		File successSubFolder = new File(successFolder + "/" + parentFolder.getName() );
            		if(successSubFolder.exists() == false) successSubFolder.mkdirs();
            		CopyFile.copyFile(file.getAbsolutePath(), successSubFolder.getAbsolutePath());
            		file.delete();
            		if(cmpOut.exists()) cmpOut.delete();
            		successCnt++;
	            	System.out.println("successCnt = " + successCnt + ", " + file.getName());
            	}
            	
            }
        }
		return successCnt;
	}
	
	public static void assertMutationSingleFolder(File folder) throws IOException, InterruptedException {
		File[] fileList = folder.listFiles();
        assert fileList != null;
        int timeoutCnt = 0;
        int successCnt = 0;
        int failedCnt = 0;
        for (File file : fileList) {
        	if(file.getName().trim().endsWith(".c") == false ) continue ;
        	String command = "cd " + file.getParent() + " && gcc -g " + file.getName() + "-lm && ./a.out";
        	List<String> execLines = testProcessThread(command);
        	
        	if(execLines.size() == 1 && execLines.get(0).trim().equals("timeout")) {
        		timeoutCnt++;
        		System.out.println("timeout:" + file.getAbsolutePath());
        		continue ;
        	}
        	boolean flag = true;
        	for(String line :execLines) {
        		if(line.matches(".*Assertion.*failed.*")) {
        			failedCnt++;
            		System.out.println("failed:" + file.getAbsolutePath());
            		flag = false;
        			break ;
        		}
        	}
        	if(flag == true) {
        		successCnt++;
            	System.out.println("successCnt = " + successCnt + ", " + file.getName());
        	}
        }
        System.out.println();
        System.out.println("timeoutCnt = " + timeoutCnt);
        System.out.println("fialedCnt = " + failedCnt);
        System.out.println("successCnt = " + successCnt);
	}
	
	public static void assertMutaion(File file) throws IOException, InterruptedException {
		String command = "cd " + file.getParent() + " && gcc -g " + file.getName() + " -lm && ./a.out";
    	List<String> execLines = testProcessThread(command);
    	if(execLines.size() == 1 && execLines.get(0).equals("timeout")) {
    		System.out.println("failed:" + file.getAbsolutePath());
    		return ;
    	}
    	for(String line :execLines) {
    		if(line.matches(".*Assertion.*failed.*")) {
        		System.out.println("failed:" + file.getAbsolutePath());
    			break ;
    		}
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
