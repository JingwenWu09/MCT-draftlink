package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fileoperation.FileDelete;
import fileoperation.getAllFileList;
import processtimer.ProcessTerminal;
import timeout.TimeoutTest;
import tools.*;

public class Main {
	public static void main(String args[]) throws IOException, InterruptedException {
		String testDir = "";
		TypeTest type = new TypeTest();
		type.testType(testDir);
	}
	
	public static void copyResultFile(String sourcedir, String targetdir) throws IOException, InterruptedException {
		File folder = new File(sourcedir);
		if(!folder.exists() || !folder.isDirectory()) {
			System.out.println(sourcedir + " not exists or is not a directory");
		}
		for(File file: folder.listFiles()){
			System.out.println(file.getName());
			if(file.getName().endsWith(".txt") && file.getName().contains("cbmc_")){
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
				if(innerFile.getName().endsWith(".txt") && innerFile.getName().contains("cbmc_")){
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

	public static void test1(){
		File file = new File("/Users/elowen/Desktop/mct-mutation-loop/Inversion");

		List<File> allFileList = new ArrayList<File>();

		getAllFileList getFileList = new getAllFileList(file);
		getFileList.getAllFile(file, allFileList);
		getFileList.compareFileList(allFileList);
		int count = 0;
		for(File f: allFileList) {
			if(f.getName().equals("cbmc.txt")) {
				count++;
				f.delete();
			}
		}
		System.out.println("count: " + count);
	}

	public static void test2() throws IOException, InterruptedException {
		String filename = "20000801-1_0.c";
		String command = "cd /Users/elowen/Desktop && cbmc " + filename + " --bounds-check --pointer-check --memory-leak-check --div-by-zero-check --signed-overflow-check --unsigned-overflow-check --pointer-overflow-check --conversion-check --undefined-shift-check --pointer-primitive-check";
		ProcessTerminal pt = new ProcessTerminal();
		List<String> execLines = pt.testProcessThread(command, 5);

		if(execLines.get(execLines.size()-1).contains("FAILED")) {
			System.out.println("yes");
			pt.testProcessThreadNotTimeLimit("cd /Users/elowen/Desktop && cp " + filename + " ./error");
		}
	}
	
}
