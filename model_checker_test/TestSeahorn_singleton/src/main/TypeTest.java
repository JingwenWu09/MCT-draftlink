package main;

import fileoperation.GeneralFile;
import processtimer.ProcessTerminal;
import tools.TestSeahorn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TypeTest {
	
	List<String> seaResultTrue = new ArrayList<String>();
	List<String> seaResultFalse = new ArrayList<String>();
	
	public void testType(String dir) throws IOException, InterruptedException {
		File folder = new File(dir);
		if(!folder.exists() || !folder.isDirectory()) {
			System.out.println(dir + " not exists or is not a directory");
		}
		
		seaResultTrue = new ArrayList<String>();
		seaResultFalse = new ArrayList<String>();
		
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

			TestSeahorn ts = new TestSeahorn();
			boolean issea = ts.testTools(file.getAbsolutePath());

			if(issea) {
				seaResultTrue.add(file.getName());
			}
			else {
				seaResultFalse.add(file.getName());
			}

		}
		rwTxt(dir, "seahorn-true", seaResultTrue, "true");
		rwTxt(dir, "seahorn-false", seaResultFalse, "false");
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
	
	public int countFile(String path){
		File singleFolder = new File(path);
		int count = 0;
		for(File file: singleFolder.listFiles()){
			if(file.getName().endsWith(".c")){
				count++;
			}
		}
		return count;
	}
	
	public void filterTimeout(String dir) throws IOException, InterruptedException {
		File folder = new File(dir);
		if(!folder.exists() || !folder.isDirectory()) {
			System.out.println(dir + " not exists or is not a directory");
		}
		
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
			
			boolean exceptsea = GeneralFile.findSameFiles("/home/elowen/桌面/seahorn-except", file.getName() + ".c");

			if(!exceptsea) {
				File seaOutput = new File(file.getAbsolutePath() + "/seahorn.txt");
				if(seaOutput.exists()) {
					TestSeahorn ts = new TestSeahorn();
					boolean issea = ts.checkAllTimeout(seaOutput);
					if(issea) {
						seaOutput.delete();
						System.out.println(seaOutput.getAbsolutePath());
					}
				}
//				if(!seaOutput.exists()) {
//					String commandi = "cd " + dir + " && cp -r " + file.getName() + " /home/elowen/桌面/Invariant-seahorn-notexec";
//					ProcessTerminal pt = new ProcessTerminal();
//					pt.testProcessThreadNotTimeLimit(commandi);
//				}
			}
		}
	}
}
