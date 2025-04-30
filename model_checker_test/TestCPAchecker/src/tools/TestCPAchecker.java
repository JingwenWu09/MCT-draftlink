package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import fileoperation.FileDelete;
import fileoperation.getAllFileList;
import processtimer.ProcessTerminal;

public class TestCPAchecker {
	List<String> successful = new ArrayList<String>();
	List<String> failure = new ArrayList<String>();
	List<String> unknown = new ArrayList<String>();
	List<String> other = new ArrayList<String>();
	List<String> timeout = new ArrayList<String>();
//	List<List<String>> lists = new ArrayList<List<String>>();
	
	public boolean testTools(String dir, String properties) throws IOException, InterruptedException {
		String commandProperties = "";
		File folder = new File(dir);
		if(!folder.exists() || !folder.isDirectory()) {
			System.out.println(dir + " not exists or is not a directory");
			return true;
		}
		
		File cpaOutput = new File(dir + "/cpachecker-" + properties + ".txt");
		if(cpaOutput.exists()) {
			return checkFalse(cpaOutput);
		}
		
		if(properties.equals("ValueAnalysis")) {
			commandProperties = "-valueAnalysis";
		}
		else if(properties.equals("PredicateAnalysis")) {
			commandProperties = "-predicateAnalysis";
		}
		else if(properties.equals("KInduction")) {
			commandProperties = "-kInduction";
		}
		else if(properties.equals("SymbolicExecution")) {
			commandProperties = "-symbolicExecution";
		}
		else if(properties.equals("OverflowChecking")) {
			commandProperties = "-predicateAnalysis--overflow -spec /home/nicai/桌面/CPAchecker-2.2-unix/config/properties/no-overflow.prp";
		}
		else if(properties.equals("SMGAnalysis")) {
			commandProperties = "-smg -spec /home/nicai/桌面/CPAchecker-2.2-unix/config/properties/valid-memsafety.prp";
		}
		else if(properties.equals("TerminationAnalysis")) {
			commandProperties = "-terminationAnalysis -spec /home/nicai/桌面/CPAchecker-2.2-unix/config/properties/termination.prp";
		} 
		
		
		File outputsFile = new File(dir + "/output");
		if(outputsFile.exists()) {
			FileDelete.deleteFolder(outputsFile);
		}
		
		List<File> allFileList = new ArrayList<File>();
		
		getAllFileList getFileList = new getAllFileList(folder);
		getFileList.getAllFile(folder, allFileList);
		getFileList.compareFileList(allFileList);
				
		for(File file: allFileList) {
			if(!file.getName().endsWith(".c")) {
				continue;
			}
			
			ProcessTerminal pt = new ProcessTerminal();
			
			String commandcpa = "cd " + file.getParent() + " && " 
					+ "/home/nicai/桌面/CPAchecker-2.2-unix/scripts/cpa.sh "
					+ commandProperties + " -preprocess -64 -setprop cpa.predicate.ignoreIrrelevantVariables=false "
					+ file.getName();
			
			List<String> execLines = pt.testProcessThread(commandcpa, 20);
			
			
			if(execLines.size() == 0) {
				System.out.println("timeout........");
				timeout.add(file.getName());
				continue;
			}                                            
			
			boolean isHaveTrue = false;
			boolean isHaveFalse = false;
			boolean isHaveUnknown = false;
			for(String s: execLines) {
				if(s.contains("Verification result: TRUE.")) {
					isHaveTrue = true;
					break;
				}
				if(s.contains("Verification result: FALSE.")) {
					isHaveFalse = true;
					break;
				}
				if(s.contains("Verification result: UNKNOWN,")) {
					isHaveUnknown = true;
					break;
				}
			}
			if(isHaveTrue) {
				System.out.println("true........");
				successful.add(file.getName());
			}
			else if(isHaveFalse) {
				System.out.println("find false........");
				failure.add(file.getName());
			}
			else if(isHaveUnknown) {
				System.out.println("unknown........");
				unknown.add(file.getName());
			}
			else {
				System.out.println("other condition........");
				other.add(file.getName());
			}
		}
		
		rwCPATxt(dir, properties);
		
//		lists.add(successful);
//		lists.add(failure);
//		lists.add(unknown);
//		lists.add(timeout);
//		lists.add(other);
//		
//		Check check = new Check();
//		return check.isConsistent(lists);
		
//		File outputsFile = new File(dir + "/output");
		if(outputsFile.exists()) {
			FileDelete.deleteFolder(outputsFile);
		}
		
		if(failure.size() != 0) {
			return false; 
		}
		
		return true;
		
	}
	
	public void rwCPATxt(String dir, String properties) {
		try {
			File file = new File(dir + "/cpachecker-" + properties + ".txt");
			
			if(file.exists()) {
				file.delete();
			}
			file.createNewFile();
			
			FileWriter fw = new FileWriter(file, true);
			PrintWriter pw = new PrintWriter(fw);
				
			pw.println("CPAchecker Model Checker Result: \n");
			pw.println("\n-----------------------------------------------------------\n");
			successful.forEach( s -> {
				pw.println(s + ": true");
			});
			
			pw.println("\n-----------------------------------------------------------\n");
			failure.forEach( s -> {
				pw.println(s + ": false");
			});
			
			pw.println("\n-----------------------------------------------------------\n");
			unknown.forEach( s -> {
				pw.println(s + ": unknown");
			});

			
			pw.println("\n-----------------------------------------------------------\n");
			timeout.forEach( s -> {
				pw.println(s + ": timeout");
			});
			
			pw.println("\n-----------------------------------------------------------\n");
			other.forEach( s -> {
				pw.println(s + ": other condition");
			});
			
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public boolean checkFalse(File file) {
		try {
			//read
			InputStreamReader in = new InputStreamReader(new FileInputStream(file));
			BufferedReader bf = new BufferedReader(in);
			
			String line = "";
			while((line = bf.readLine()) != null) {
				if(line.trim().contains(": false")) {
					return false;
				}
			}
			bf.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return true; 
	}

	
}
