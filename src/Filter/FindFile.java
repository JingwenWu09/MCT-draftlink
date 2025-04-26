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

public class FindFile {

	public static void main(String[] args) {
		File sourceFolder;
		File destFolder;
		
		sourceFolder = new File("/home/jing/桌面/modifyCFile/apList");
		//sourceFolder = "/home/jing/桌面/modifyCFile/vector";
		destFolder = new File("/home/jing/桌面/Switch");
		FindFile.findFile(sourceFolder, destFolder);
		System.out.println();
		
		return ;
	}

	public static void findFile(File sourceFolder, File destFolder) {
		try { 
			//在destFolder里找存在于sourceFolder中的文件
			System.out.println("sourceFolder:\n" + sourceFolder);
			System.out.println("destFolder:\n" + destFolder);
			
			int replaceCnt = 0;
			Set<String> sourceFolderFile = new HashSet<String>();
			if(sourceFolder.exists()) {
				for(File file: sourceFolder.listFiles()) {
					sourceFolderFile.add(file.getName());
				}
			} else System.out.println("sourceFolder not exist!");
			
			if(destFolder.exists()) {
				for(File file: destFolder.listFiles()) {
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
