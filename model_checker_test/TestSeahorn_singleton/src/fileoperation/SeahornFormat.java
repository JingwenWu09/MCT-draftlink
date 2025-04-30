package fileoperation;

import java.io.*;

public class SeahornFormat {
	public File transFormat(File file) {
		try {
			//read
			String filename = file.getName();
			BufferedReader bf = null;
			InputStreamReader in = new InputStreamReader(new FileInputStream(file));
			bf = new BufferedReader(in);
			
			//write
			File newFile = new File(file.getParent()+ "/" +filename.substring(0, filename.lastIndexOf("."))+"_seahorn" + filename.substring(filename.lastIndexOf(".")));

			if(newFile.exists()) {
				newFile.delete();
			}
			newFile.createNewFile();
			FileWriter fw = new FileWriter(newFile, true);
			PrintWriter pw = new PrintWriter(fw);
				
			String line = "";
			while((line = bf.readLine()) != null) {
				if(line.trim().matches(".*#include\\s*<assert.h>.*")) {
					line = line.replace("<assert.h>", " \"seahorn/seahorn.h\"");
				}
				if(line.trim().matches("assert\\s*\\(.*")) {
					line = line.replace("assert", "sassert");
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
}
