package fileoperation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GeneralFile {
	public static boolean findSameFiles(String dirs, String filename) {
		File dir = new File(dirs);
		List<File> allFileList = new ArrayList<File>();
		
		getAllFileList getFileList = new getAllFileList(dir);
		getFileList.getAllFile(dir, allFileList);
		
		for(File f: allFileList) {
			if(f.getName().equals(filename)) {
				return true;
			}
		}
		return false;
	}
}
