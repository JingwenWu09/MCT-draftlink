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
import java.util.List;
import java.util.Set;

public class CombineResult {

	public static void main(String[] args) {
		File srcFolder = new File("/home/jing/Desktop/Unrolling");
		File destFolder = new File("/home/jing/Desktop/mct-result-multi-config/Unrolling");
//		copyFalseWholeFolder(folder, destFolder);
		combineResultTxt(srcFolder, destFolder);
		System.out.println("end!");
	}
	
	public static void combineResultTxt(File srcFolder, File destFolder){
		if(!srcFolder.exists()) {
			return ;
		}

		int OverflowCnt = 0;
		int SMGCnt = 0;
		int SymbolicCnt = 0;
		int ValueCnt = 0;
		for(File subFolder: srcFolder.listFiles()) {
			if(subFolder.isDirectory() == false) continue ;
			
			File resultTxt1 = new File(subFolder + "/cpachecker-OverflowChecking.txt");
			if(resultTxt1.exists()) {
				OverflowCnt ++;
				copyFile(resultTxt1, new File(destFolder + "/" + subFolder.getName()));
			}
			
			File resultTxt2 = new File(subFolder + "/cpachecker-SMGAnalysis.txt");
			if(resultTxt2.exists()) {
				SMGCnt ++;
				copyFile(resultTxt2, new File(destFolder + "/" + subFolder.getName()));
			}
			
			File resultTxt3 = new File(subFolder + "/cpachecker-SymbolicExecution.txt");
			if(resultTxt3.exists()) {
				SymbolicCnt ++;
				copyFile(resultTxt3, new File(destFolder + "/" + subFolder.getName()));
			}
			
			File resultTxt4 = new File(subFolder + "/cpachecker-ValueAnalysis.txt");
			if(resultTxt4.exists()) {
				ValueCnt ++;
				copyFile(resultTxt4, new File(destFolder + "/" + subFolder.getName()));
			}
			
//			if(OverflowCnt != SMGCnt || OverflowCnt != SymbolicCnt || SMGCnt != SymbolicCnt) {
//				System.out.println(subFolder.getName());
//			}
			
		}
		
		System.out.println("OverflowCnt = " + OverflowCnt);
		System.out.println("SMGCnt = " + SMGCnt);
		System.out.println("SymbolicCnt = " + SymbolicCnt);
		System.out.println("ValueCnt = " + ValueCnt);

		return ;
	}
	
	public static void copyFile(File srcFile, File destFolder){
		//System.out.println(dir);
		try { 
				
			if(destFolder.exists() == false || destFolder.isDirectory() == false) {
				System.out.println("destFolder not exist!");
				//destFolder.mkdirs();
			}
			
//			if(destDir.endsWith("/") == false) destDir += "/";
			
			File newFile = new File(destFolder.getAbsolutePath() + "/" + srcFile.getName());
			if(newFile.exists()) {
				newFile.delete();
//				System.out.println(newFile.getName() + " already exist!");
//				return ;
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
