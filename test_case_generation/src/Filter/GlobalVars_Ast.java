package Filter;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.model.ArrayVar;
import AST_Information.model.AstVariable;
import AST_Information.model.FunctionBlock;

public class GlobalVars_Ast {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Set<String> globalVarsFileName = new HashSet<String>();
		Set<String> globalVarsPointerFileName = new HashSet<String>();
		
		int notCfile = 0;
		int globalVarFile = 0;
		int globalVarsPointerFile = 0;
		int fileCnt = 0;
		
		String copyDestGlobalAll = "/home/jing/MCTesting/GlobalVars_Ast/dataflow/GlobalVarsCFile/";
		String copyDestPointer = "/home/jing/MCTesting/GlobalVars_Ast/dataflow/GlobalVarsPointer/";
		File folder = new File("/home/jing/MCTesting/filter-c-testsuite/dataflow/");
		if(folder.exists()) {
			File[] filelist = folder.listFiles();
			fileCnt = filelist.length;
			for( int i = 0; i < filelist.length; i++) {
				String filename = filelist[i].getName();
				
				if(filename.endsWith(".c") == false) {
					notCfile++;
					continue ;
				}

				String filepath = filelist[i].getAbsolutePath();
				System.out.println("i = " + i);
				System.out.println(filepath);
				AstInform_Gen astgen = new AstInform_Gen();
				astgen.getAstInform(filepath);
				List<FunctionBlock> allFunctionBlocks = astgen.getAllFunctionBlocks();
				Set<String> globalVarsSet = astgen.getGlobalVarsSet(astgen.allVarsMap, allFunctionBlocks);
				
				if(globalVarsSet.size() > 0) {
					globalVarFile++;
					globalVarsFileName.add(filename);
					CopyFile.copyFile(folder + "/" +  filename, copyDestGlobalAll);
				}
				
//				for(String varid: globalVarsSet) {
//					AstVariable var = astgen.allVarsMap.get(varid);
//					if(var.getKind().equals("pointer")) {
//						globalVarsPointerFile++;
//						globalVarsPointerFileName.add(filename);
//						CopyFile.copyFile(folder + "/" +  filename, copyDestPointer);
//						System.out.println("globalPointer: " + var.getName() + " "  + var.getType() + ", " + var.getDeclareLine());
//						break ;
//					}else if(var.getKind().equals("array")) {
//						ArrayVar arrayVar = (ArrayVar)var;
//						if(arrayVar.getElementKind().equals("pointer")) {
//							globalVarsPointerFile++;
//							globalVarsPointerFileName.add(filename);
//							CopyFile.copyFile(folder + "/" +  filename, copyDestPointer);
//							System.out.println("globalPointer: " + var.getName() + " "  + var.getType() + ", " + var.getDeclareLine());
//							break ;
//						}
//					}
//				}
				System.out.println("fileCnt = " + fileCnt);
				System.out.println("notCfile = " + notCfile);
				System.out.println("globalVarFile = " + globalVarFile);
				System.out.println("globalVarsPointerFile = " + globalVarsPointerFile);
				System.out.println();
			}
			
			System.out.println("fileCnt = " + fileCnt);
			System.out.println("\nnotCfile = " + notCfile);
			System.out.println("\nglobalVarFile = " + globalVarFile);
//			for(String name: globalVarsFileName) {
//				System.out.println(name);
//				CopyFile.copyFile(folder + "/" +  name, copyDest);
//				
//			}
//			String copyDest = "/home/jing/MCTesting/GlobalVars_Ast/dataflow/";
//			System.out.println("\nglobalVarsPointerFile = " + globalVarsPointerFile);
//			for(String name: globalVarsPointerFileName) {
//				System.out.println(name);
//				CopyFile.copyFile(folder + "/" +  name, copyDest);
//			}
		}
		
	}

}
