package fileoperation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class FileDelete {
	public static void deleteFolder(File file){
	      for (File subFile : file.listFiles()) {
	         if(subFile.isDirectory()) {
	            deleteFolder(subFile);
	         } else {
	            subFile.delete();
	         }
	      }
	      file.delete();
	   }
	public static void rwFile(File file, String dir) {
		try {
			//read
//			String filename = file.getName();
			BufferedReader bf = null;
			InputStreamReader in = new InputStreamReader(new FileInputStream(file));
			bf = new BufferedReader(in);
			
			//write
			File newFile = new File(dir+file.getName());

			if(newFile.exists()) {
				newFile.delete();
//				newFile = new File(dir+filename.substring(0, filename.lastIndexOf("."))+"_"+file.getParent().substring(file.getParent().lastIndexOf("/")+1)+filename.substring(filename.lastIndexOf(".")));
			}
			newFile.createNewFile();
			FileWriter fw = new FileWriter(newFile, true);
			PrintWriter pw = new PrintWriter(fw);
				
			String line = "";
			while((line = bf.readLine()) != null) {
				pw.println(line);
			}
			bf.close();
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
