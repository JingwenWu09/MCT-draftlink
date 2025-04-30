package Filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class CopyFailedFile {
	
	public static void main(String[] args) {
		
		File folder = new File("/home/jing/桌面/If_DeleteFabs_Result");
		File destFolder = new File("/home/jing/桌面/If_Cpa_False_True");
//		copyFalseWholeFolder(folder, destFolder);
		copyFalseFolder(folder, destFolder);
		System.out.println("end!");
	}
	
	//复制有false结果的整个文件夹，包括initTest, trueMut, falseMut, resultTxt
	public static void copyFalseWholeFolder(File folder, File destFolder){
		File resultFalseTxt = new File(folder.getAbsolutePath() + "/cpachecker-false.txt");
		if(!resultFalseTxt.exists()) {
			return ;
		}
		System.out.println("has txt");
		
		InputStreamReader ins;
		BufferedReader br = null;
		
		int copyFileCnt = 0;
		int falseFolderCnt = 0;
		try {
			ins = new InputStreamReader(new FileInputStream(resultFalseTxt));
			br = new BufferedReader(ins);
			String line = null;
			while((line = br.readLine()) != null) {
				line = line.trim();
				if(line.matches(".*\\.c:\\s+false")) {
					falseFolderCnt++;
//					System.out.println(line);
					String copyFolderName = line.substring(0, line.lastIndexOf(".c"));
					File srcFolder = new File(folder.getAbsolutePath() + "/" + copyFolderName);
					for(File child: srcFolder.listFiles()) {
						if(child.getName().endsWith(".c") == false && child.getName().equals("cpachecker.txt") == false) {
							continue ;
						}
						copyFile(child.getAbsolutePath(), new File(destFolder.getAbsolutePath() + "/" + copyFolderName) );
						copyFileCnt++;
					}
				}
			}
			System.out.println("falseFolderCnt = " + falseFolderCnt);
			System.out.println("copyFileCnt = " + copyFileCnt);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	//复制有false结果的文件夹，及该文件夹下的falseMut;或者falseMut和trueMut分别放进false和true文件夹
	public static void copyFalseFolder(File folder, File destFolder){
		File resultFile = new File(folder.getAbsolutePath() + "/cpachecker-false.txt");
		if(!resultFile.exists()) {
			return ;
		}
		System.out.println("has txt");
		
		InputStreamReader ins;
		BufferedReader br = null;
		
		int falseFolderCnt = 0;
		int initialCnt = 0;
		int falseCnt = 0;
		int trueCnt = 0;
		try {
			ins = new InputStreamReader(new FileInputStream(resultFile));
			br = new BufferedReader(ins);
			String line = null;
			while((line = br.readLine()) != null) {
				line = line.trim();
				if(line.matches(".*\\.c:\\s+false")) {
					falseFolderCnt++;
//					System.out.println(line);
					String falseFolderName = line.substring(0, line.lastIndexOf(".c"));
					String initialName = falseFolderName +".c";
					File resultTxt = new File(folder.getAbsolutePath() + "/" + falseFolderName + "/cpachecker.txt");
					List<Set<String>> resultMutNameSet = getResultMutNameSet(resultTxt);
					File subFolder = new File(folder.getAbsolutePath() + "/" + falseFolderName);
					for(File child: subFolder.listFiles()) {
						//initial
						if(child.getName().equals(initialName)) {
							copyFile(child.getAbsolutePath(), new File(destFolder.getAbsolutePath() + "/" + falseFolderName) );
							initialCnt++;
						}
						//false .c
						if( resultMutNameSet.get(0).contains(child.getName()) ){
							copyFile(child.getAbsolutePath(), new File(destFolder.getAbsolutePath() + "/" + falseFolderName + "/false") );
							falseCnt++;
						}
						//true .c
						if(resultMutNameSet.get(1).contains(child.getName()) && child.getName().equals(initialName) == false){
							copyFile(child.getAbsolutePath(), new File(destFolder.getAbsolutePath() + "/" + falseFolderName + "/true") );
							trueCnt++;
						}
						
					}
				}
			}
			System.out.println("falseFolderCnt = " + falseFolderCnt);
			System.out.println("initialCnt = " + initialCnt);
			System.out.println("falseCnt = " + falseCnt);
			System.out.println("trueCnt = " + trueCnt);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static List<Set<String>> getResultMutNameSet(File resultTxt) {
		List<Set<String>> resultMutNameSet = new ArrayList<Set<String>>();
		Set<String> falseNameSet = new HashSet<String>();
		Set<String> trueNameSet = new HashSet<String>();
		try {
			InputStreamReader ins;
			BufferedReader br = null;
			ins = new InputStreamReader(new FileInputStream(resultTxt));
			br = new BufferedReader(ins);
			String line = null;
			while((line = br.readLine()) != null) {
				line = line.trim();
				if(line.matches(".*\\.c:\\s+false")) {
					falseNameSet.add(line.substring(0,line.lastIndexOf(":")));
				}else if(line.matches(".*\\.c:\\s+true")) {
					trueNameSet.add(line.substring(0,line.lastIndexOf(":")));
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		resultMutNameSet.add(falseNameSet);
		resultMutNameSet.add(trueNameSet);
		return resultMutNameSet;
	}
	
	public static void copyFile(String srcFilepath, File destFolder){
		//System.out.println(dir);
		try { 
				
			if(destFolder.exists() == false || destFolder.isDirectory() == false) destFolder.mkdirs();
			
//			if(destDir.endsWith("/") == false) destDir += "/";
			
			File srcFile = new File(srcFilepath);
			File newFile = new File(destFolder.getAbsolutePath() + "/" + srcFile.getName());
			if(newFile.exists()) {
				newFile.delete();
			}
			newFile.createNewFile();
			
			FileWriter fw = new FileWriter(newFile, true);
			PrintWriter pw = new PrintWriter(fw);
			InputStream input = null;  
			OutputStream output = null;   
			
			input = new FileInputStream(srcFile); 
			output = new FileOutputStream(newFile); 
			byte[] buf = new byte[1024];  
			int bytesRead; 
			while ((bytesRead = input.read(buf)) > 0) { 
				output.write(buf, 0, bytesRead); 
			} 
			input.close(); 
			output.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
