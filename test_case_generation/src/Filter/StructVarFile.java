package Filter;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.model.AstVariable;

public class StructVarFile {

	public Set<String> structFileName;
	public int allFileCnt = 0;
	public int cFileCnt = 0;
	public int notCFile = 0;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sourceFolder;
		String destFolder;
		
//		sourceFolder= "/home/jing/MCTesting/filter-c-testsuite/dataflow/";
//		destFolder = "/home/jing/MCTesting/GlobalVars/GlobalVars_ClangS/dataflow/";
//		GlobalVars_ClangS.getGlobalVarsInform(sourceFolder, destFolder);
//		System.out.println();
		
		sourceFolder= "/home/jing/MCTesting/filter-c-testsuite/loop/all/";
		destFolder = "/home/jing/MCTesting/UnionVarFile/loop/all/";
		StructVarFile.searchStructVarFile(sourceFolder, destFolder);
		System.out.println();
	}
	
	public static void searchStructVarFile(String sourceFolder, String destFolder) {
		
		StructVarFile struct = new StructVarFile();
		struct.structFileName = new HashSet<String>();
		
		File folder = new File(sourceFolder);
		if(folder.exists()) {
			File[] filelist = folder.listFiles();
			struct.allFileCnt = filelist.length;
			
			for( int i = 0; i < filelist.length; i++) {
				String filename = filelist[i].getName();
				
				if(filename.endsWith(".c") == false) {
					struct.notCFile++;
					continue ;
				}
				struct.cFileCnt++;
				System.out.println("i = " + i + ", " + filename);
				AstInform_Gen astgen = new AstInform_Gen();
				astgen.getAstInform(filelist[i].getAbsolutePath());
				for(AstVariable var: astgen.allVarsMap.values()) {
					if(var.getType().contains("union ")) {
						//System.out.println(var.getType() +" " + var.getName() + " " + var.getDeclareLine());
						struct.structFileName.add(filename);
						CopyFile.copyFile(folder + "/" +  filename, destFolder);
						break ;
					}
				}
			}
		}
		
		System.out.println("sourceFolder:\n" + sourceFolder);
		System.out.println("destFolder:\n" + destFolder);
		System.out.println("allFileCnt = " + struct.allFileCnt);
		System.out.println("cFileCnt = " + struct.cFileCnt);
		System.out.println("notCFileCnt = " + struct.notCFile);
		System.out.println("structFileCnt = " + struct.structFileName.size());
//		for( String filename: struct.structFileName) {
//			System.out.println(filename);
//		}
		
		return ;
	}
	
}
