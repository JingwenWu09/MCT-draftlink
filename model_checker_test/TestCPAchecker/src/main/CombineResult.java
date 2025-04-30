package main;

import java.io.File;

import fileoperation.FileDelete;

public class CombineResult {
	public static void main(String args[]) {
		File folder = new File("/media/elowen/KESU/seahorn/CSElimination");
		if(!folder.exists() || !folder.isDirectory()) {
			System.out.println("dir not exists or is not a directory");
		}
		System.out.println(folder.listFiles().length);
		int count = 0;
		for(File file: folder.listFiles()) {
			System.out.println(file.getName());
			if(file.getName().endsWith(".txt")) {
				FileDelete.rwFile(file, "/home/elowen/桌面/mct-result/dataflow/CSElimination/");
				continue;
			}
			if(file.listFiles().length == 1) {
				continue;
			}
			for(File f: file.listFiles()) {
				if(f.getName().equals("seahorn.txt")) {
					FileDelete.rwFile(f, "/home/elowen/桌面/mct-result/dataflow/CSElimination/" + file.getName() + "/");
					count++;
					break;
				}
			}
		}
		System.out.println(count + " end!");
	}
}
