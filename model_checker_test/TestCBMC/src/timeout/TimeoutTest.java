package timeout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fileoperation.FileDelete;
import fileoperation.SeahornFormat;
import fileoperation.getAllFileList;
import processtimer.ProcessTerminal;

public class TimeoutTest {
	public void testTimeout() {
		File dir = new File("/home/jing/Desktop/dataflow");
		List<File> allFileList = new ArrayList<File>();
		
		getAllFileList getFileList = new getAllFileList(dir);
		getFileList.getAllFile(dir, allFileList);
		getFileList.compareFileList(allFileList);
		
		int count = 0;
		for(File file: allFileList) {
			if(!file.getName().endsWith(".c")) {
				continue;
			}
			System.out.println(++count+":  "+file.getName());
			
			try {
				System.out.println("start CBMC:");
				testCBMC(file);
				System.out.println("start CPAchecker:");
				testCPAchecker(file);
				System.out.println("start Seahorn:");
				testSeahorn(file);
				file.delete();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testCBMC(File file) throws IOException, InterruptedException {
		String command = "cd " + file.getParent() + " && " + "cbmc " + file.getName();
		ProcessTerminal pt = new ProcessTerminal();
		List<String> execLines = pt.testProcessThread(command, 6);
		if(execLines.size() == 0) {
			ProcessTerminal ptt = new ProcessTerminal();
			execLines = ptt.testProcessThread("cd " + file.getParent() + " && " + "cbmc " + file.getName() + " --unwind 10", 3);
			if(execLines.size() == 0) {
				FileDelete.rwFile(file, "/home/jing/Desktop/cbmc-timeout/");
				return;
			}
		}
		if(!execLines.get(execLines.size()-1).contains("VERIFICATION SUCCESSFUL") 
				&& !execLines.get(execLines.size()-1).contains("VERIFICATION FAILURE")) {
			FileDelete.rwFile(file, "/home/jing/Desktop/cbmc-other/");
		}
	}
	
	public void testCPAchecker(File file) throws IOException, InterruptedException {
		String prefileName = file.getName().substring(0, file.getName().lastIndexOf(".c")) + ".i";
		String commandi = "cd " + file.getParent() + " && gcc -E " + file.getName() + " -o " + prefileName;
		ProcessTerminal pt = new ProcessTerminal();
		pt.testProcessThread(commandi, 3);
		File prefile = new File(file.getParent() + "/" + prefileName);
		if(!prefile.exists()) {
			FileDelete.rwFile(file, "/home/jing/Desktop/cpa-other/");
			return;
		}
		
		String commandcpa = "cd " + file.getParent() + " && " 
				+ "/home/jing/Desktop/CPAchecker-2.2-unix/scripts/cpa.sh -default " 
				+ prefileName;
		List<String> execLines = pt.testProcessThread(commandcpa, 20);
		if(execLines.size() == 0) {
			FileDelete.rwFile(file, "/home/jing/Desktop/cpa-timeout/");
			prefile.delete();
			return;
		}
		
		boolean isHaveResult = false;
		for(String s: execLines) {
			if(s.contains("Verification result: TRUE.") ||
					s.contains("Verification result: FALSE.") ||
					s.contains("Verification result: UNKNOWN,")) {
				isHaveResult = true;
				break;
			}
		}
		if(!isHaveResult) {
			FileDelete.rwFile(file, "/home/jing/Desktop/cpa-other/");
		}
		prefile.delete();
	}
	
	public void testSeahorn(File file) throws IOException, InterruptedException {
		SeahornFormat sf = new SeahornFormat();
		File seaFile = sf.transFormat(file);
		String partPath = seaFile.getParent().replace("/home/jing/Desktop", "/host");
		String command = "cd /home/jing/Desktop && docker exec pensive_fermat bash -c \"cd " + partPath
				+ " && sea pf -m64 " + seaFile.getName() + "\"";
		ProcessTerminal pt = new ProcessTerminal();
		List<String> execLines = pt.testProcessThread(command, 10);
		if(execLines.size() == 0) {
			FileDelete.rwFile(file, "/home/jing/Desktop/seahorn-timeout/");
			seaFile.delete();
			return;
		}
		if(!execLines.get(execLines.size()-1).contains("unsat") &&
				!execLines.get(execLines.size()-1).contains("sat")) {
			FileDelete.rwFile(file, "/home/jing/Desktop/seahorn-other/");
		}
		seaFile.delete();
	}
}
