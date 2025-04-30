package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import fileoperation.*;

import processtimer.ProcessTerminal;

public class CheckSeahornResult {
	static Map<Integer, String> individualStatement = new TreeMap<Integer, String>();
	static int index = 0;
	
	public static void main(String argc[]) {
		File folder11 = new File("/home/jing/Desktop/Sea-False-Folder/If/seahorn-false");
		Run(folder11);
		
		File folder12 = new File("/home/jing/Desktop/seahorn-bugs/Sea-False-Folder/If/seahorn-false");
		Run(folder12);
		
		File folder1 = new File("/home/jing/Desktop/seahorn-bugs/Sea-False-Folder/ConstantFolding/seahorn-false");
		Run(folder1);
		File folder2 = new File("/home/jing/Desktop/seahorn-bugs/Sea-False-Folder/CSElimination/seahorn-false");
		Run(folder2);
		File folder3 = new File("/home/jing/Desktop/seahorn-bugs/Sea-False-Folder/DataFlowSR/seahorn-false");
		Run(folder3);
		File folder4 = new File("/home/jing/Desktop/seahorn-bugs/Sea-False-Folder/Fusion/seahorn-false");
		Run(folder4);
//		File folder5 = new File("/home/jing/Desktop/seahorn-bugs/Sea-False-Folder/Invariant/seahorn-false");
//		Run(folder5);
//		File folder6 = new File("/home/jing/Desktop/seahorn-bugs/Sea-False-Folder/Inversion/seahorn-false");
//		Run(folder6);
//		File folder7 = new File("/home/jing/Desktop/seahorn-bugs/Sea-False-Folder/IVElimination/seahorn-false");
//		Run(folder7);
//		File folder8 = new File("/home/jing/Desktop/seahorn-bugs/Sea-False-Folder/IVElimination_pow/seahorn-false");
//		Run(folder8);
//		File folder9 = new File("/home/jing/Desktop/seahorn-bugs/Sea-False-Folder/Switch/seahorn-false");
//		Run(folder9);
//		File folder10 = new File("/home/jing/Desktop/seahorn-bugs/Sea-False-Folder/Unswitching/seahorn-false");
//		Run(folder10);
		
	}
	
	public static void Run(File folder) {
		
		File[] listFiles = folder.listFiles();
		Arrays.sort(listFiles, new Comparator< File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });
		for(File file: listFiles) {
			System.out.println(file.getName());

			if(file.getName().equals(".DS_Store")) {
				continue;
			}
			if(file.getName().endsWith(".txt")) {
				continue;
			}
			if(file.listFiles().length == 1) {
				continue;
			}
			testOneInitCase(file);
		}
		
		System.out.println("end!");
		
	}
	
	public static void testOneInitCase(File folder){
		File seaResultFile = new File(folder.getAbsoluteFile() + "/seahorn.txt");
		List<String> satFileList = getSatFileList(seaResultFile);
		int first = 0;
		for(String filename: satFileList) {
			File initFile = new File(folder.getAbsoluteFile() + "/" + filename);
			if(isUnsat(initFile)) {
				writeResultFile(initFile, null, ++first, true);
				continue;
			}
			index = 0;
			individualStatement = new TreeMap<Integer, String>();
			Map<Integer, String> individualResult = individualAssertResult(initFile);
			writeResultFile(initFile, individualResult, ++first, false);
		}
		
		if(checkAllUnsat(folder)) {
			FileDelete.deleteFolder(folder);
		}
		
	}
	//sat return false
	public static boolean isUnsat(File initFile) {
		try {
			SeahornFormat sf = new SeahornFormat();
			File allAssertSeaFile = sf.transFormat(initFile);
			String command = "cd " + allAssertSeaFile.getParent() + 
					" && sea bpf -m64 --bmc=opsem --promote-nondet-undef=false " + allAssertSeaFile.getName();
			
			ProcessTerminal pt = new ProcessTerminal();
			List<String> execLines = pt.testProcessThread(command, 15);
			pt.killSeahornThread();
			
			if(execLines.size() == 0) {
				return true;
			}
			
			for(String s: execLines) {
				if(s.trim().matches("Warning: found [0-9]+ possible reads of undefined values")) {
					return true;
				}
			}
			
			if(execLines.get(execLines.size()-1).trim().equals("sat")){
				return false;
			}
			else {
				return true;
			}
				
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static List<String> getSatFileList(File seaResultFile){
		List<String> falseNameList = new ArrayList<String>();
		try {
			InputStreamReader ins = new InputStreamReader(new FileInputStream(seaResultFile));
			BufferedReader br = new BufferedReader(ins);
			String line = null;
			while((line = br.readLine()) != null) {
				line = line.trim();
				if(line.matches(".*\\.c:\\s+sat")) {
					falseNameList.add(line.substring(0,line.lastIndexOf(":")).trim());
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return falseNameList;
	}
	
	public static Map<Integer, String> individualAssertResult(File initFile){
		Map<Integer, String> resultMap = new TreeMap<Integer, String>();
		try {
			InputStreamReader ins = new InputStreamReader(new FileInputStream(initFile));
			BufferedReader br = new BufferedReader(ins);
			String line = null;
			int count = 0;
			while((line = br.readLine()) != null) {
				count++;
				line = line.trim();
				if(line.contains("assert") && !line.contains("<assert.h>")) {
					File seaFile = transFormat(initFile, count);
					String command = "cd " + seaFile.getParent() + " && sea bpf -m64 --bmc=opsem " + seaFile.getName();
					
					ProcessTerminal pt = new ProcessTerminal();
					List<String> execLines = pt.testProcessThread(command, 15);
					pt.killSeahornThread();
					
//					for(String s: execLines) {
//						System.out.println(s);
//					}
					
					if(execLines.size() == 0) {
						resultMap.put(count, "timeout");
					}
					else {
						String seaResult = "";
						if(execLines.get(execLines.size()-1).trim().equals("unsat")){
							seaResult = "unsat";
//							System.out.println("unsat.........");
						}
						else if(execLines.get(execLines.size()-1).trim().equals("sat")){
							seaResult = "sat";
//							System.out.println("find sat.........");
						}
						else {
							seaResult = "other condition";
//							System.out.println("other condition.........");
						}
						
						for(String s: execLines) {
							if(s.contains("Warning: found 1 possible reads of undefined values")) {
								seaResult += "  --found undefined values";
								break;
							}
						}
						
						resultMap.put(count, seaResult);
					}
					
					seaFile.delete();
				}
			}
			
			return resultMap;
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static File transFormat(File file, int lineCount) {
		try {
			//read
			String filename = file.getName();
			BufferedReader bf = null;
			InputStreamReader in = new InputStreamReader(new FileInputStream(file));
			bf = new BufferedReader(in);
			
			//write
			File newFile = new File(file.getParent()+ "/" +filename.substring(0, filename.lastIndexOf("."))+"_seahorn_temp_" + (index++) + filename.substring(filename.lastIndexOf(".")));
			if(newFile.exists()) {
				newFile.delete();
			}
			newFile.createNewFile();
			FileWriter fw = new FileWriter(newFile, true);
			PrintWriter pw = new PrintWriter(fw);
				
			String line = "";
			int count = 0;
			while((line = bf.readLine()) != null) {
				count++;
				if(line.trim().matches(".*#include\\s*<assert.h>.*")) {
					line = line.replace("<assert.h>", " \"seahorn/seahorn.h\"");
				}
				else if(line.trim().matches("assert\\s*\\(.*")) {
					if(count == lineCount) {
						line = line.replace("assert", "sassert");
						individualStatement.put(count, line.trim());
					}
					else {
						line = "";
					}
				}
				pw.println(line);
			}
			bf.close();
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
			return newFile;
		}catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static void writeResultFile(File file, Map<Integer, String> individualResult, int first, boolean isUnsat) {
		try {
			File newFile = new File(file.getParent() + "/seahorn-individual.txt");
			if(first == 1) {
				if(newFile.exists()) {
					newFile.delete();
				}
				newFile.createNewFile();
			}
			
			FileWriter fw = new FileWriter(newFile, true);
			PrintWriter pw = new PrintWriter(fw);
			
			pw.println("------ " + file.getName() + "_seahorn ------ ");
			
			if(isUnsat) {
				pw.print("------ unsat ------");
			}
			else {
				for(int key: individualResult.keySet()) {
					System.out.println("第" + key + "行： " + individualStatement.get(key) + "   : " + individualResult.get(key));
					pw.println("第" + key + "行： " + individualStatement.get(key) + "   : " + individualResult.get(key));
				}
			}
			
			pw.println("\n\n");
			
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static boolean checkAllUnsat(File folder) {
		try {
			//read
			File file = new File(folder.getAbsolutePath() + "/seahorn-individual.txt");
			BufferedReader bf = null;
			InputStreamReader in = new InputStreamReader(new FileInputStream(file));
			bf = new BufferedReader(in);
			
			String line = "";
			while((line = bf.readLine()) != null) {
				if(line.contains(": sat") || line.contains(": other condition")) {
					return false;
				}
			}
			bf.close();
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
}