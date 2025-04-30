package Filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GlobalVars_ClangS {
	
	public Set<String> globalVarFileName;
	public Set<String> globalVarsPointerFileName;
	public Set<String> notExistClangSFileName;	//没有生成.ll的.c文件
	public int existClangSFileCnt = 0;
	public int allFileCnt = 0;
	public int notCFile = 0;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sourceFolder;
		String destFolder;
		
//		sourceFolder= "/home/jing/MCTesting/filter-c-testsuite/dataflow/";
//		destFolder = "/home/jing/MCTesting/GlobalVars/GlobalVars_ClangS/dataflow/";
//		GlobalVars_ClangS.getGlobalVarsInform(sourceFolder, destFolder);
//		System.out.println();
		
		sourceFolder= "/home/elowen/桌面/MCTestsuite/initialTestSuite/condition/Switch/";
		destFolder = "/home/elowen/桌面/MCTestsuite/GlobalVars/GlobalVars_ClangS/condition/switch/";
		GlobalVars_ClangS.getGlobalVarsInform(sourceFolder, destFolder);
		System.out.println();
		
//		sourceFolder= "/home/jing/MCTesting/filter-c-testsuite/loop/dowhile/";
//		destFolder = "/home/jing/MCTesting/GlobalVars/GlobalVars_ClangS/loop/dowhile/";
//		GlobalVars_ClangS.getGlobalVarsInform(sourceFolder, destFolder);
//		System.out.println();
//		
//		sourceFolder= "/home/jing/MCTesting/filter-c-testsuite/loop/for/";
//		destFolder = "/home/jing/MCTesting/GlobalVars/GlobalVars_ClangS/loop/for/";
//		GlobalVars_ClangS.getGlobalVarsInform(sourceFolder, destFolder);
//		System.out.println();
//		
//		sourceFolder= "/home/jing/MCTesting/filter-c-testsuite/loop/while/";
//		destFolder = "/home/jing/MCTesting/GlobalVars/GlobalVars_ClangS/loop/while/";
//		GlobalVars_ClangS.getGlobalVarsInform(sourceFolder, destFolder);
//		System.out.println();
		
//		sourceFolder= "/home/jing/MCTesting/filter-c-testsuite/condition/if/";
//		destFolder = "/home/jing/MCTesting/GlobalVars/GlobalVars_ClangS/condition/if/";
//		GlobalVars_ClangS.getGlobalVarsInform(sourceFolder, destFolder);
//		System.out.println();
//		
//		sourceFolder= "/home/jing/MCTesting/filter-c-testsuite/condition/switch/";
//		destFolder = "/home/jing/MCTesting/GlobalVars/GlobalVars_ClangS/condition/switch/";
//		GlobalVars_ClangS.getGlobalVarsInform(sourceFolder, destFolder);
//		System.out.println();
		
		return ;
	}
	
	public static void getGlobalVarsInform(String sourceFolder, String destFolder) {
		
		GlobalVars_ClangS globalVars = new GlobalVars_ClangS();
		globalVars.globalVarFileName = new HashSet<String>();
		globalVars.notExistClangSFileName = new HashSet<String>();
		globalVars.globalVarsPointerFileName = new HashSet<String>();
		File folder = new File(sourceFolder);
		if(folder.exists()) System.out.println("exist");
		else {
			System.out.println("not exist");
			System.out.println(sourceFolder);
		}
		if(folder.exists()) {
			File[] filelist = folder.listFiles();
			globalVars.allFileCnt = filelist.length;
			System.out.println(filelist.length);
			for( int i = 0; i < filelist.length; i++) {
				String filename = filelist[i].getName();
				
				if(filename.endsWith(".c") == false) {
					globalVars.notCFile++;
					continue ;
				}
				
//				System.out.println("i = " + i + ", filename: " + filename);
				globalVars.getGlobalVarsPointerSet(sourceFolder, filename, destFolder);
			}
		}
		
		System.out.println("sourceFolder:\n" + sourceFolder);
		System.out.println("destFolder:\n" + destFolder);
		System.out.println("allFileCnt = " + globalVars.allFileCnt);
		System.out.println("notCFileCnt = " + globalVars.notCFile);
		System.out.println("existClangSFileCnt = " + globalVars.existClangSFileCnt);
		System.out.println("notExistClangSFileCnt = " + globalVars.notExistClangSFileName.size());
		System.out.println("globalVarFileCnt = " + globalVars.globalVarFileName.size());
		System.out.println("globalPointerFileCnt = " + globalVars.globalVarsPointerFileName.size());
		
		for(String filename: globalVars.globalVarFileName) {
			CopyFile.copyFile(folder + "/" +  filename, destFolder + "GlobalVarsCFile/");
		}
		
		for(String filename: globalVars.globalVarsPointerFileName) {
			CopyFile.copyFile(folder + "/" +  filename, destFolder + "GlobalVarsPointer/");
		}
		
		GlobalVars_ClangS.genNotExistClangSFileTxt(globalVars.notExistClangSFileName, destFolder);
		GlobalVars_ClangS.copyNotExistClangSFile(sourceFolder, destFolder);
		
		return ;
	}
	
	public void getGlobalVarsPointerSet(String sourceFolder, String filename, String destFolder){
		String filepath = destFolder + "/ClangS_File_ll/" + filename.substring(0, filename.lastIndexOf(".")) + ".ll";
		//String filepath = "/home/jing/MCTesting/filter-c-testsuite/GlobalVars/NotExistFile/temp.ll";
		//Set<String> globalVarsSet = new HashSet<String>();
		
		File file = new File(filepath);
		if(file.exists() == false) {
			this.notExistClangSFileName.add(filename);
			return ;
		}
		this.existClangSFileCnt++;
		
		String line;
		boolean hasGlobalVar = false;
		try {
			InputStreamReader ins =  new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(ins);
			while((line = br.readLine()) != null) {
//				System.out.println("line: " + line);
				if(hasGlobalVar == false) {
					if(line.startsWith("@") == true) {
						this.globalVarFileName.add(filename);
						String name = line.substring(1, line.indexOf(" "));
						line = line.substring(line.indexOf(" global ") + 8);
						char ch = line.charAt(0);
						switch(ch) {
							case '[':
								line = line.substring(0, line.indexOf(" ", line.indexOf("]")));
								break;
							case '<':
								line = line.substring(0, line.indexOf(" ", line.indexOf(">")));
								break;
							case '{':
								line = line.substring(0, line.indexOf(" ", line.indexOf("}")));
								break;
							default:
								line = line.substring(0, line.indexOf(" "));
						}
//						if(line.charAt(0) == '[') line = line.substring(0, line.indexOf(" ", line.indexOf("]")));
//						else line = line.substring(0, line.indexOf(" "));
//						System.out.println(line);
						if(line.contains("*")) {
							this.globalVarsPointerFileName.add(filename);
//							System.out.println(name + " " + line);
						}
						hasGlobalVar = true;
					}else continue ;
				}else {
					if(line.startsWith("@") == true) {
						String name = line.substring(1, line.indexOf(" "));
						line = line.substring(line.indexOf(" global ") + 8);
						char ch = line.charAt(0);
						switch(ch) {
							case '[':
								line = line.substring(0, line.indexOf(" ", line.indexOf("]")));
								break;
							case '<':
								line = line.substring(0, line.indexOf(" ", line.indexOf(">")));
								break;
							case '{':
								line = line.substring(0, line.indexOf(" ", line.indexOf("}")));
								break;
							default:
								line = line.substring(0, line.indexOf(" "));
						}
//						if(line.charAt(0) == '[') line = line.substring(0, line.indexOf(" ", line.indexOf("]")));
//						else line = line.substring(0, line.indexOf(" "));
//						System.out.println(line);
						if(line.contains("*")) {
							this.globalVarsPointerFileName.add(filename);
//							System.out.println(name + " " + line);	
						}
					}else break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return ;
	}
	
	public static void genNotExistClangSFileTxt(Set<String> notExistClangSFileName, String destFolder){
		String filepath = destFolder + "notExistClangSFileName.txt";
		//Set<String> globalVarsSet = new HashSet<String>();
		
		try {
			File file = new File(filepath);
			if(file.exists()) {
				file.delete();
			}
			file.createNewFile();

			FileWriter fw = new FileWriter(file, true);
			PrintWriter pw = new PrintWriter(fw);
			
			for(String name: notExistClangSFileName) {
				pw.println(name); 
			}
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	
	public static void copyNotExistClangSFile(String sourceFolder, String destFolder) {
		String filepath = destFolder + "/notExistClangSFileName.txt";
		//Set<String> globalVarsSet = new HashSet<String>();
		
		File file = new File(filepath);
		if(file.exists() == false) {
			return ;
		}
	
		String line;
		try {
			InputStreamReader ins =  new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(ins);
			while((line = br.readLine()) != null) {
				CopyFile.copyFile(sourceFolder + line.replaceAll("\\s*$", ""), destFolder + "notExistClangSFile/All/");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return ;
	}

}
