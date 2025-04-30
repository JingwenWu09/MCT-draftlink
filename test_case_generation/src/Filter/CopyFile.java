package Filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class CopyFile {
	
	public static void main(String[] args) {
		File copyFolder = new File("/home/jing/桌面/Invariant_backup/assertFailed");
		String destFolder = "/home/jing/桌面/Redone";
		String srcFolder = "/home/jing/桌面/filter-c-testsuite/loop/all";
		int cnt = 0;
		if(copyFolder.exists()) {
			for(File folder: copyFolder.listFiles()) {
				if(folder.isDirectory() == false) continue ;
				copyFile(srcFolder + "/" + folder.getName() + ".c", destFolder);
				cnt ++;
			}
			System.out.println("cnt = " + cnt);
		}else {
			System.out.println("copyFolder not exist!");
		}
	}
	
	public static void copyFile(String srcFilepath, String destDir){
		//System.out.println(dir);
		try { 
			
			File destFolder = new File(destDir);
			if(destFolder.exists() == false || destFolder.isDirectory() == false) destFolder.mkdirs();
			
//			if(destDir.endsWith("/") == false) destDir += "/";
			
			File srcFile = new File(srcFilepath);
			File newFile = new File(destFolder.getAbsolutePath() + "/" + srcFile.getName());
			if(!srcFile.exists()) {
				System.out.println("src File: " + srcFile.getName() + " not exist!");
				return ;
			}
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
