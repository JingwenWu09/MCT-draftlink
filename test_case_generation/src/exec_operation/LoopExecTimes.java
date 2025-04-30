package exec_operation;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import processtimer.ProcessTerminal;


public class LoopExecTimes {
	//for() for(;;);
	public static int getTimes(File file, List<String> initialList, int startLine, int endLine) throws IOException, InterruptedException {
		int loopExecTimes = 0;
		String filename = file.getName();
		String newExecFileName = filename.substring(0, filename.lastIndexOf("."))+"_execTime"+filename.substring(filename.lastIndexOf("."));
		File newFile = new File(file.getParent() + "/" + newExecFileName);
		if(newFile.exists()) {
			newFile.delete();
		}
		newFile.createNewFile();
		
		FileWriter fw = new FileWriter(newFile, true);
		PrintWriter pw = new PrintWriter(fw);
		
		for(int i=1; i<=initialList.size();i++) {
			if(i == startLine) {
				pw.println("int countLoopTimes = 0;");
				pw.println(initialList.get(i-1));
				pw.println("countLoopTimes++;");
				continue;
			}
			if(i == endLine) {
				pw.println(initialList.get(i-1));
				pw.println("printf(\"countLoopTimes = %d\\n\", countLoopTimes);");
				continue;
			}
			pw.println(initialList.get(i-1));
		}
		
		pw.flush();
		fw.flush();
		pw.close();
		fw.close();
		
		//终端运行c文件
		String command = "cd " + file.getParent() + " && " + "gcc -g " + newExecFileName + " -lm && " + "./a.out";
		
		ProcessTerminal pt = new ProcessTerminal();
		List<String> execLines = pt.testProcessThread(command, 5);
		
		if(execLines.size() == 0) {
			newFile.delete();
			return 0;
		}
		
		for(String s: execLines) {
			if(s.contains("countLoopTimes")) {
				String timesStr = "";
				for(int i = 0; i < s.length(); i++) {
					if((s.charAt(i) >= 48) && (s.charAt(i)<=57)) {
						timesStr += s.charAt(i);
					}
				}
				try {
					loopExecTimes = Integer.parseInt(timesStr);
				}catch(NumberFormatException e) {
					newFile.delete();
					return 0;
				}
			}
		}
		newFile.delete();
		return loopExecTimes;
	}
	
}
