package Filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.model.AstVariable;

public class UnusedVar {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//UnusedVar.FilterUnusedVar("/home/jing/MCTesting/dataflow/");
		File folder = new File("/home/jing/MCTesting/DeadCode/");
		
		UnusedVar.FilterUnusedVar("/home/jing/MCTesting/dataflow/");
	}
	
	public static void PrintUsedVar(String filepath) {
		File file = new File(filepath);
		if(file.exists()) {
			AstInform_Gen astgen = new AstInform_Gen();
			astgen.getAstInform(filepath);
			Iterator<AstVariable> it = astgen.allVarsMap.values().iterator();
			List<AstVariable> unusedVar = new ArrayList<AstVariable>();
			while(it.hasNext()) {
				AstVariable var = it.next();
				if(var.getIsUsed() == false) {
					unusedVar.add(var);
				}
			}
			
			System.out.println(filepath);
			System.out.println("unusedVar: " + unusedVar.size());
			for(AstVariable var: unusedVar) {
				System.out.println(var.getName() + " " + var.getType() + " "
						+ var.getDeclareLine() + " " + var.getIsParmVar());
			}
		}
	}
	
	public static void FilterUnusedVar(String fileFolder) throws IOException {
		File folder = new File(fileFolder);
		if(folder.exists()) {
			File filelist[] = folder.listFiles();
			//System.out.println(folder.listFiles().length);
			int Cnt = 0;
			for(File file: filelist) { 
				if(file.exists()) {
					String filepath = file.getAbsolutePath();
					System.out.println(filepath);
					AstInform_Gen astgen = new AstInform_Gen();
					astgen.getAstInform(filepath);
					Iterator<AstVariable> it = astgen.allVarsMap.values().iterator();
					boolean has_unUsedVar = false;
					while(it.hasNext()) {
						AstVariable var = it.next();
						if(var.getIsParmVar() == false && var.getIsUsed() == false) {
							has_unUsedVar = true;
							break ;
						}
					}
					if(has_unUsedVar == true) {
						Cnt++;
						UnusedVar.CopyFile(file);
					}
				}
			}
			
			System.out.println("\nunUsedVarFile: " + Cnt);
			
		}else System.out.println("folder not exist!");
		
	}

	private static void CopyFile(File src) throws IOException {
			//System.out.println(dir);
			String destDir = "/home/jing/MCTesting/DeadCode/UnusedVar/";
			File newFile = new File(destDir + src.getName());
			if(newFile.exists()) {
				newFile.delete();
			}
			newFile.createNewFile();
			FileWriter fw = new FileWriter(newFile, true);
			PrintWriter pw = new PrintWriter(fw);
			
    	  InputStream input = null;   
    	  OutputStream output = null;   
    	  try { 
    	      input = new FileInputStream(src); 
    	      output = new FileOutputStream(newFile);     
    	      byte[] buf = new byte[1024];     
    	      int bytesRead;     
    	      while ((bytesRead = input.read(buf)) > 0) { 
    	        output.write(buf, 0, bytesRead); 
    	      } 
    	  } finally { 
    	    input.close(); 
    	    output.close(); 
    	  } 
	}
	
}
