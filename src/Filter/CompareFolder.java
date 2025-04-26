package Filter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompareFolder {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path1 = "/home/jing/MCTesting/GlobalVars_Ast/dataflow/GlobalVarsPointer/";
		String path2 = "/home/jing/MCTesting/GlobalVars_ClangS/GlobalVarsPointer/";
		List<List<String>> diffFile = CompareFolder.getDifferentFileList(path1, path2);
		
		
		CompareFolder.genTxtFormList(diffFile.get(0), "/home/jing/桌面/differ_1.txt");
		CompareFolder.genTxtFormList(diffFile.get(1), "/home/jing/桌面/differ_2.txt");
		
		System.out.println("folder1 not exist:");
		for(String name: diffFile.get(0)) {
			System.out.println(name);
		}
		System.out.println();
		System.out.println("folder2 not exist:");
		for(String name: diffFile.get(1)) {
			System.out.println(name);
		}
		
	}
	
	public static List<List<String>> getDifferentFileList(String path1, String path2){
		
		List<List<String>> diffFile = new ArrayList<List<String>>();
		List<String> diff1 = new ArrayList<String>();
		List<String> diff2 = new ArrayList<String>();
		
		File folder1 = new File(path1);
		File folder2 = new File(path2);
		
		Set<String> f1 = new HashSet<String>();
		Set<String> f2 = new HashSet<String>();
		
		for(File file: folder1.listFiles()){
			f1.add(file.getName());
		}
		for(File file: folder2.listFiles()){
			String name = file.getName();
			f2.add(name);
			if(f1.contains(name) == false) {
				diff1.add(name);
			}
		}
		
		for(String name: f1) {
			if(f2.contains(name) == false) {
				diff2.add(name);
			}
		}
		
		diffFile.add(diff1);
		diffFile.add(diff2);
		
		return diffFile;
		
	}
	
	public static void genTxtFormList(List<String> list, String destPath){
		//Set<String> globalVarsSet = new HashSet<String>();
		
		try {
			File file = new File(destPath);
			if(file.exists()) {
				file.delete();
			}
			file.createNewFile();

			FileWriter fw = new FileWriter(file, true);
			PrintWriter pw = new PrintWriter(fw);
			
			for(String name: list) {
				pw.println(name); 
			}
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
