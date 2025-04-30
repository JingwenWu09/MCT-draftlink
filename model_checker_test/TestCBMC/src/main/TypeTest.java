package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import fileoperation.GeneralFile;
import fileoperation.getAllFileList;
import processtimer.ProcessTerminal;
import tools.*;

public class TypeTest {
	String dir = "";
	Map<String, Map<String, Boolean>> allFolderResult = new TreeMap<String, Map<String, Boolean>>();
	
	public void testType(String dir) throws IOException, InterruptedException {
		this.dir = dir;
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

//			if(countFile(file.getAbsolutePath()) > 100){
//				String commandi = "cd " + dir + " && cp -r " + file.getName() + " /Users/elowen/Desktop/mct-mutation-loop/Unrolling-sizeover100";
//				ProcessTerminal pt = new ProcessTerminal();
//				pt.testProcessThreadNotTimeLimit(commandi);
//				continue;
//			}
			
			boolean exceptcbmc = GeneralFile.findSameFiles("/home/elowen/桌面/cbmc-except", file.getName() + ".c");

			if(!exceptcbmc) {
				TestCBMC tc = new TestCBMC();
				tc.testTools(file.getAbsolutePath());
				allFolderResult.put(file.getName(), tc.allResult);
			}
		}
		createAllResultFiles();
		rwResultTxt();
	}

	public void createAllResultFiles() {
		try {
//			String[] properties = {"assertions", "bounds-check", "pointer-check", "memory-leak-check", "div-by-zero-check", "signed-overflow-check", "unsigned-overflow-check", "pointer-overflow-check", "conversion-check", "undefined-shift-check", "pointer-primitive-check"};
			String[] properties = {"assertions", "bounds-check", "pointer-check"};
			for (String p : properties) {
				File fileTrue = new File(dir + "/cbmc_true_" + p + ".txt");
				File fileFalse = new File(dir + "/cbmc_false_" + p + ".txt");
				if (fileTrue.exists()) {       
					fileTrue.delete();
				}
				if (fileFalse.exists()) {
					fileFalse.delete();
				}
				fileTrue.createNewFile();
				fileFalse.createNewFile();
			}
		}catch (IOException e){
			e.printStackTrace();
		}

	}
	
	public void rwResultTxt() {
		try {
			for(Map.Entry<String, Map<String, Boolean>> entry: allFolderResult.entrySet()){
				String foldername = entry.getKey();
				Map<String, Boolean> singleFolderResult = entry.getValue();
				for(Map.Entry<String, Boolean> innerEntry: singleFolderResult.entrySet()){
					String property = innerEntry.getKey();
					boolean isResult = innerEntry.getValue();
					appendContentToFile(foldername, property, isResult);
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void appendContentToFile(String foldername, String property, boolean isResult){
		try {
			File file = new File(dir + "/cbmc_" + isResult + "_" + property + ".txt");
			FileWriter fw = new FileWriter(file, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(foldername + ".c: " + isResult);
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
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
}
