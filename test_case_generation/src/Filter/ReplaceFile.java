package Filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class ReplaceFile {
	
	public static void main(String[] args) {
		File sourceFolder;
		File replaceFolder;
		
		sourceFolder = new File("/home/jing/桌面/IfCondition/if-testsuite");
		//sourceFolder = "/home/jing/桌面/modifyCFile/vector";
		replaceFolder = new File("/home/jing/桌面/modify/modify");
		ReplaceFile.replaceFile(sourceFolder, replaceFolder);
		System.out.println();
		
		return ;
	}

	public static void replaceFile(File sourceFolder, File replaceFolder) {
		try { 
			//replaceFolder替换sourceFolder
			System.out.println("sourceFolder:\n" + sourceFolder);
			System.out.println("replaceFolder:\n" + replaceFolder);
			
			int replaceCnt = 0;
			Set<String> sourceFolderFile = new HashSet<String>();
			if(sourceFolder.exists()) {
				for(File file: sourceFolder.listFiles()) {
					sourceFolderFile.add(file.getName());
				}
			} else System.out.println("sourceFolder not exist!");
			
			if(replaceFolder.exists()) {
				for(File file: replaceFolder.listFiles()) {
					String filename = file.getName();
					if(filename.endsWith(".c") == false) continue ;
					if(sourceFolderFile.contains(filename)) {
						replaceCnt++;
						
						File newFile = new File(sourceFolder + "/" + filename);
						if(newFile.exists()) {
							newFile.delete();
						}
						newFile.createNewFile();
						
						FileWriter fw = new FileWriter(newFile, true);
						PrintWriter pw = new PrintWriter(fw);
						InputStream input = null;  
						OutputStream output = null;   
						
						input = new FileInputStream(file); 
						output = new FileOutputStream(newFile); 
						byte[] buf = new byte[1024];  
						int bytesRead; 
						while ((bytesRead = input.read(buf)) > 0) { 
							output.write(buf, 0, bytesRead); 
						} 
						input.close(); 
						output.close(); 
					}
				}
				System.out.println("replaceCnt = " + replaceCnt);
			}else System.out.println("replaceFolder not exist!");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
