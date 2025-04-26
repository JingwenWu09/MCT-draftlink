package Filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class DeleteFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File sourceFolder;
		File deleteFolder;
		
		sourceFolder = new File("/home/jing/Desktop/randomtestsuite（复件）/timeout");
		deleteFolder = new File("/home/jing/Desktop/randomtestsuite");
		DeleteFile.deleteFile(sourceFolder, deleteFolder);
		System.out.println("end!");
		
		return ;
	}
	
	public static void deleteFolder(File sourceFolder, File destFolder) {
		System.out.println("sourceFolder:\n" + sourceFolder);
		System.out.println("destFolder:\n" + destFolder);
		
		int deleteFolderCnt = 0;
		if(sourceFolder.exists()) {
			for(File file: sourceFolder.listFiles()) {
				String filename = file.getName();
				String deleteFolderName = filename.substring(0, filename.lastIndexOf(".c"));
				File deleteFolder = new File(destFolder.getAbsolutePath() + "/" + deleteFolderName);
				if(deleteFolder.exists()) {
					deleteFolderCnt++;
					for(File deleteFile: deleteFolder.listFiles()) {
						deleteFile.delete();
					}
					deleteFolder.delete();
				}
			}
			System.out.println("finished! deleteFolderCnt = " + deleteFolderCnt);
		} else System.out.println("sourceFolder not exist!");
		
	}
	public static void deleteFile(File sourceFolder, File deleteFolder) {
		//在sourceFolder中删除deleteFolder中存在的文件
		System.out.println("sourceFolder:\n" + sourceFolder);
		System.out.println("deleteFolder:\n" + deleteFolder);
		
		int deleteCnt = 0;
		if(sourceFolder.exists()) {
			for(File file: sourceFolder.listFiles()) {
				String filename = file.getName();
				if(filename.endsWith(".c") == false) continue ;
				File deleteFile = new File(deleteFolder.getAbsolutePath() + "/" + filename);
				System.out.println(deleteFile.getAbsolutePath());
				if(deleteFile.exists()) {
					deleteCnt++;
					deleteFile.delete();
					System.out.println("deleteFile: " + filename);
					String deleteCopy = "/home/jing/桌面/all-delete";
					CopyFile.copyFile(deleteFile.getAbsolutePath(), deleteCopy);
				}
			}
			System.out.println("deleteCnt = " + deleteCnt);
		}else System.out.println("sourceFolder not exist!");
	}

}
