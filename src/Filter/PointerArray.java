package Filter;

import java.io.File;

import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.model.ArrayVar;
import AST_Information.model.AstVariable;

public class PointerArray {
	
	public static void main(String[] args) {
		
		String sourceFolder = "/home/elowen/桌面/ModelCheckingTesting/filter-c-testsuite/loop/all";
		String destFOlder = "/home/elowen/桌面/test";
		PointerArray.searchPointerArray(sourceFolder, destFOlder);
	}

	public static void searchPointerArray(String sourceFolder, String destFolder) {
		File folder = new File(sourceFolder);
		int pointerArrayCnt = 0;
		int allFileCnt = 0;
		int cFileCnt = 0;
		int notCFileCnt = 0;
		if(folder.exists()) {
			File[] filelist = folder.listFiles();
			allFileCnt = filelist.length;
			for( int i = 0; i < filelist.length; i++) {
				String filename = filelist[i].getName();
				
				if(filename.endsWith(".c") == false) {
					notCFileCnt++;
					continue ;
				}
				cFileCnt++;
				
//				System.out.println("i = " + i + ", filename: " + filename);
				
				
				AstInform_Gen astgen = new AstInform_Gen();
				astgen.getAstInform(sourceFolder + filename);
				
//				for(AstVariable var: astgen.allVarsMap.values()) {
//					System.out.println(var.getType() + " " + var.getName() + " " + var.getDeclareLine());
//				}
//				
//				System.out.println();
				
				for(AstVariable var: astgen.allVarsMap.values()) {
					if(var.getKind().equals("array")) {
						ArrayVar arrayVar = (ArrayVar)var;
						if(arrayVar.getElementKind().equals("pointer")) {
							CopyFile.copyFile(sourceFolder + filename, destFolder);
							pointerArrayCnt++;
							System.out.println("i = " + i + ", filename: " + filename);
//							System.out.println("pointerArrayCnt = " + pointerArrayCnt);
							break ;
						}
					}
				}
			}
			
			System.out.println("sourceFolder: " + sourceFolder);
			System.out.println("allFileCnt = " + allFileCnt);
			System.out.println("cFileCnt = " + cFileCnt);
			System.out.println("notCFileCnt = " + notCFileCnt);
			System.out.println("pointerArrayCnt = " + pointerArrayCnt);
		}
		
	}

}
