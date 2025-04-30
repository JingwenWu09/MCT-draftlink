package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fileoperation.FileDelete;
import fileoperation.getAllFileList;
import processtimer.ProcessTerminal;
import tools.*;

public class Main {
	public static void main(String args[]) throws IOException, InterruptedException {
		TypeTest type = new TypeTest();

		type.testType("/home/nicai/桌面/ConstantFolding", "ValueAnalysis");
		System.out.println("end ValueAnalysis!");
		
		type.testType("/home/nicai/桌面/ConstantFolding", "PredicateAnalysis");
		System.out.println("end PredicateAnalysis!");
		
		type.testType("/home/nicai/桌面/ConstantFolding", "KInduction");
		System.out.println("end kinduction!");
		
		type.testType("/home/nicai/桌面/ConstantFolding", "OverflowChecking");
		System.out.println("end OverflowChecking!");
		
		type.testType("/home/nicai/桌面/ConstantFolding", "SMGAnalysis");
		System.out.println("end SMGAnalysis!");

//		type.filterFalseFile("/home/nicai/桌面/mct-result-multi-config/Unswitching", "SMGAnalysis");
//		deleteOutputDir("/home/nicai/桌面/Inversion");
		
//		CountResult cs = new CountResult();
//		cs.countCPACheck("/home/nicai/桌面/Inversion", "cpachecker-SymbolicExecution.txt");

//		copyResultFile("/home/nicai/桌面/If", "/home/nicai/桌面/mct-result-multi-config/If");
		
		System.out.println("end!!!");
	}

	public static void copyResultFile(String sourcedir, String targetdir) throws IOException, InterruptedException {
		File folder = new File(sourcedir);
		if(!folder.exists() || !folder.isDirectory()) {
			System.out.println(sourcedir + " not exists or is not a directory");
		}
		for(File file: folder.listFiles()){
			System.out.println(file.getName());
			if(file.getName().endsWith(".txt") && file.getName().contains("cpachecker-")){
				String commandi = "cd " + sourcedir + " && cp " + file.getName() + " " + targetdir;
				ProcessTerminal pt = new ProcessTerminal();
				pt.testProcessThreadNotTimeLimit(commandi);
				continue;
			}
			if(file.getName().equals(".DS_Store")) {
				continue;
			}
			if(file.getName().endsWith(".txt")) {
				continue;
			}
			if(file.listFiles().length == 1) {
				continue;
			}
			for(File innerFile: file.listFiles()){
				if(innerFile.getName().endsWith(".txt") && innerFile.getName().contains("cpachecker-")){
					System.out.println("exists..........");
					String commandi = "cd " + file.getAbsolutePath() + " && cp " + innerFile.getName() + " " + targetdir + "/" + file.getName();
					ProcessTerminal pt = new ProcessTerminal();
					pt.testProcessThreadNotTimeLimit(commandi);
				}
			}

		}
	}
	
	public static void deleteOutputDir(String dir) {
		File folder = new File(dir);
		for(File file: folder.listFiles()) {
			if(file.isDirectory()) {
				if(file.getName().equals("output")) {
					System.out.println(file.getAbsolutePath());
					FileDelete.deleteFolder(file);
				}
				else {
					deleteOutputDir(file.getAbsolutePath());
				}
			}
		}
	}
	
}
