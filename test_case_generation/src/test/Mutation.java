package test;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import AssrtFilter.RunAssert;
import Condition.IfCondition;
import Condition.SwitchCondition;
import DataFlow.CSElimination;
import DataFlow.ConstantFolding;
import DataFlow.DataFlowSR;
import DataFlow.DeadCode;
import Filter.CopyFile;
import file_operation.getAllFileList;

public class Mutation {
	public static void main(String[] args) throws ClassCastException, IOException, InterruptedException {
		
		File sourceFolder;
		File destFolder;
		File countFolder;
		
		sourceFolder = new File("/home/jing/桌面/IfRedone");
//		destFolder = new File("/home/jing/桌面/IfCondition/If");
		destFolder = new File("/home/jing/Desktop/randomtestsuite（复件）");
		//生成mutation
//		Mutation.genMutation(sourceFolder, destFolder);
		//assert判断 
		FilterAssert(new File(destFolder.getAbsolutePath() + ""));

//		countFolder = new File(destFolder.getAbsolutePath() + "/assertFailed");
//		System.out.println( "DestFolderCnt:" + Mutation.getFolderCFileCount(countFolder, 0) );

		return ;
	}
	
	public static void FilterAssert(File destFolder) throws IOException, InterruptedException {
		long successCnt = RunAssert.assertMutation(destFolder, 0, destFolder);
		System.out.println("successCnt = " + successCnt);
	}

	
	public static void genMutation(File sourceFolder, File destFolder) throws ClassCastException, IOException, InterruptedException {
		
		String mutationType = destFolder.getName();

		if(sourceFolder.exists() == false) {
			System.out.println(sourceFolder + "not exist\n");
			return ;
		}
			
		if( destFolder.exists() == false) {
			System.out.println(destFolder + "not exist\n");
			return ;
		}
		
		getAllFileList getFileList = new getAllFileList();
		List<File> allFileList = new ArrayList<File>();
		getFileList.getAllFile(sourceFolder, allFileList);
		getFileList.compareFileList(allFileList);
		System.out.println(allFileList.size());

		for( int i = 0; i < allFileList.size(); i++) {
			File file = allFileList.get(i);
			String filename = file.getName();
			if(filename.endsWith(".c") == false) continue ;
//			if(filename.equals("use-after-scope-switch-3.c") == false) continue ;
			
			System.out.println("i:" + i + " " + filename);
			
			String mutationDir = destFolder.getAbsolutePath() + "/" + filename.substring(0, filename.lastIndexOf(".c"));
			File mutationFolder = new File(mutationDir);
			if(!mutationFolder.exists()) {
				mutationFolder.mkdirs();
				System.out.println("mk dir: " + mutationDir);
			}
			CopyFile.copyFile(file.getAbsolutePath(), mutationDir);
			
			if(mutationType.equals("If")) {
				IfCondition ifCondition = new IfCondition();
				ifCondition.Transformation(file, mutationDir);
			}
			else if(mutationType.equals("Switch")) {
				SwitchCondition switchCondition = new SwitchCondition();
				switchCondition.Transformation(file, mutationDir);
			}
			else if(mutationType.equals("ConstantFolding")) {
				ConstantFolding cf = new ConstantFolding();
				cf.Transformation(file, mutationDir);
			}
			else if(mutationType.equals("CSElimination")){
				CSElimination cse = new CSElimination();
				cse.Transformation(file, mutationDir);
			}
			else if(mutationType.equals("DataFlowSR")) {
				DataFlowSR sr = new DataFlowSR();
				sr.Transformation(file, mutationDir);
			}
			else if(mutationType.equals("DeadCode")) {
				DeadCode deadCode = new DeadCode();
				deadCode.Transformation(file, mutationDir);
			}
			
//			File childFolder = new File(mutationDir);
//			if(childFolder.listFiles().length > 1) {
//				file.delete();
//			}
			file.delete();
		}

		System.out.println("end!");
	}
	
	public static long getFolderCFileCount(File folder, long cnt) {
		if(!folder.exists()) {
			System.out.println("folder not exist!");
			return 0;
		}
		long tempCnt = cnt;
        File[] fileList = folder.listFiles();
        assert fileList != null;
//        System.out.println(fileList.length);
        for (File file : fileList) {
            if (file.isDirectory()) {
//            	if(file.listFiles().length == 1) continue;
                cnt += getFolderCFileCount(file, tempCnt);
            } else { 
            	if(file.getName().trim().endsWith(".c") == true) {
            		cnt++;
            	}
            }
        }
        return cnt;
	}

}
