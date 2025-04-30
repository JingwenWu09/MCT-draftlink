package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import fileoperation.getAllFileList;

public class CountResult {
	public void countCPACheck(String dir, String filename) throws IOException, InterruptedException {
		File folder = new File(dir);
		if(!folder.exists() || !folder.isDirectory()) {
			System.out.println(dir + " not exists or is not a directory");
		}
		
		List<File> allFileList = new ArrayList<File>();
		
		getAllFileList getFileList = new getAllFileList(folder);
		getFileList.getAllFile(folder, allFileList);
		getFileList.compareFileList(allFileList);
		
		int countTrue = 0;
		int countFalse = 0;
		int countUnknown = 0;
		int countTimeout = 0;
		int countOther = 0;
		for(File file: allFileList) {
			if(file.getName().equals(filename)) {
				try {
					InputStreamReader in = new InputStreamReader(new FileInputStream(file));
					BufferedReader bf = new BufferedReader(in);
					
					String line = "";
					while((line = bf.readLine()) != null) {
						if(line.trim().contains(": true")) {
							countTrue++;
						}
						else if(line.trim().contains(": false")) {
							countFalse++;
						}
						else if(line.trim().contains(": unknown")) {
							countUnknown++;
						}
						else if(line.trim().contains(": timeout")) {
							countTimeout++;
						}
						else if(line.trim().contains(": other condition")) {
							countOther++;
						}
					}
					bf.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Total: " + (countTrue + countFalse + countUnknown + countTimeout + countOther) );
		System.out.println("True: " + countTrue);
		System.out.println("False: " + countFalse);
		System.out.println("Unknown: " + countUnknown);
		System.out.println("Timeout: " + countTimeout);
		System.out.println("Other condition: " + countOther);
	}
}
