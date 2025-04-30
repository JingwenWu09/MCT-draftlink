package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fileoperation.GeneralFile;
import fileoperation.getAllFileList;
import processtimer.ProcessTerminal;
import tools.*;

public class TypeTest {
	
	List<String> cpaResultTrue = new ArrayList<String>();
	List<String> cpaResultFalse = new ArrayList<String>();

	public void testType(String dir, String properties) throws IOException, InterruptedException {
		File folder = new File(dir);
		if(!folder.exists() || !folder.isDirectory()) {
			System.out.println(dir + " not exists or is not a directory");
		}
		
		cpaResultTrue = new ArrayList<String>();
		cpaResultFalse = new ArrayList<String>();
		
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
		
		int count = 0;
		for(File file: listFiles) {
			System.out.println(file.getName());
			System.out.println(++count);
			if(file.getName().equals(".DS_Store")) {
				continue;
			}
			if(file.getName().endsWith(".txt")) {
				continue;
			}
			if(file.listFiles().length == 1) {
				continue;
			}
			
			boolean exceptcpa = GeneralFile.findSameFiles("/home/nicai/桌面/cpa-except", file.getName() + ".c");

			if(!exceptcpa) {
				TestCPAchecker tp = new TestCPAchecker();
				boolean iscpa = tp.testTools(file.getAbsolutePath(), properties);
				if(iscpa) {
					cpaResultTrue.add(file.getName());
//					String commandi = "cd " + dir + " && cp -r " + file.getName() + " /home/nicai/桌面/inversion-timeout";
//					ProcessTerminal pt = new ProcessTerminal();
//					pt.testProcessThreadNotTimeLimit(commandi);
				}
				else {
					cpaResultFalse.add(file.getName());
				}
			}
		}
		rwTxt(dir, "cpachecker-true-"+properties, cpaResultTrue, "true");
		rwTxt(dir, "cpachecker-false-"+properties, cpaResultFalse, "false");
	}
	
	public void rwTxt(String dir, String txtName, List<String> writeList, String result) {
		try {
			File file = new File(dir + "/" + txtName + ".txt");
			
			if(file.exists()) {
				file.delete();
			}
			file.createNewFile();
			
			FileWriter fw = new FileWriter(file, true);
			PrintWriter pw = new PrintWriter(fw);
				
			pw.println("Transformation Model Check EMI Result: \n");
			pw.println("\n-----------------------------------------------------------\n");
			writeList.forEach( s -> {
				pw.println(s + ".c: " + result);
			});
			
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void filterFalseFile(String dir, String properties) throws IOException, InterruptedException {
		File folder = new File(dir);
		if(!folder.exists() || !folder.isDirectory()) {
			System.out.println(dir + " not exists or is not a directory");
		}
		
		cpaResultTrue = new ArrayList<String>();
		cpaResultFalse = new ArrayList<String>();
		
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
		
		int count = 0;
		for(File file: listFiles) {
			System.out.println(file.getName());
			System.out.println(++count);
			if(file.getName().equals(".DS_Store")) {
				continue;
			}
			if(file.getName().endsWith(".txt")) {
				continue;
			}
			if(file.listFiles().length == 1) {
				continue;
			}
			
			TestCPAchecker tp = new TestCPAchecker();
			boolean iscpa = tp.testTools(file.getAbsolutePath(), properties);
			if(!iscpa) {
				String commandi = "cd " + dir + " && cp -r " + file.getName() + " /home/nicai/桌面/CPAbugs/Unswitching/smg";
				ProcessTerminal pt = new ProcessTerminal();
				pt.testProcessThreadNotTimeLimit(commandi);
			}
			
		}
	}
	
}
