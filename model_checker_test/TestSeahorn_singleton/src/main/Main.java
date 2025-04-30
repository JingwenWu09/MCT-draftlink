package main;

import fileoperation.FileDelete;
import fileoperation.GeneralFile;
import fileoperation.SeahornFormat;
import processtimer.ProcessTerminal;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String args[]) throws IOException, InterruptedException {
		String testDir = "";
		TypeTest type = new TypeTest();
		type.testType(testDir);
		ProcessTerminal pt = new ProcessTerminal();
		pt.killSeahornThread();
	}

	public static void copySeahornFile(String sourcedir, String targetdir) throws IOException, InterruptedException {
		File folder = new File(sourcedir);
		if(!folder.exists() || !folder.isDirectory()) {
			System.out.println(sourcedir + " not exists or is not a directory");
		}
		int count = 0;
		for(File file: folder.listFiles()) {
			System.out.println(file.getName());
//			if(file.getName().equals("seahorn-skip")){
//				continue;
//			}
//			if(file.getName().equals("seahorn-false.txt")){
//				String commandi = "cd " + sourcedir + " && cp seahorn-false.txt " + targetdir;
//				ProcessTerminal pt = new ProcessTerminal();
//				pt.testProcessThreadNotTimeLimit(commandi);
//				continue;
//			}
//			if(file.getName().equals("seahorn-true.txt")){
//				String commandi = "cd " + sourcedir + " && cp seahorn-true.txt " + targetdir;
//				ProcessTerminal pt = new ProcessTerminal();
//				pt.testProcessThreadNotTimeLimit(commandi);
//				continue;
//			}
			if (file.getName().equals(".DS_Store")) {
				continue;
			}
			if (file.getName().endsWith(".txt")) {
				continue;
			}
			if (file.listFiles().length == 1) {
				continue;
			}
			File seaOutput = new File(file.getAbsolutePath() + "/seahorn.txt");
			if (seaOutput.exists()) {
				count++;
				System.out.println("exist");
				String commandi = "cd " + file.getAbsolutePath() + " && cp seahorn.txt " + targetdir + "/" + file.getName();
				ProcessTerminal pt = new ProcessTerminal();
				pt.testProcessThreadNotTimeLimit(commandi);
			}
		}
		System.out.println(count);
	}

	public static void deleteOutputDir(String dir) {
		File folder = new File(dir);
		for(File file: folder.listFiles()) {
			if(file.isDirectory()) {
				if(file.getName().equals("output")) {
					System.out.println(file.getAbsolutePath());
					FileDelete.deleteFolder(file);
				} else {
					deleteOutputDir(file.getAbsolutePath());
				}
			}
		}
	}

	public static void deleteSeahornFile(String dir) {
		File folder = new File(dir);
		for(File file: folder.listFiles()) {
			if(file.isDirectory()) {
				deleteSeahornFile(file.getAbsolutePath());
			}
			else{
				if(file.getName().contains("seahorn")){
					System.out.println(file.getAbsolutePath());
					file.delete();
				}
			}
		}
	}

}
