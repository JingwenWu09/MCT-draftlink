package Filter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileArrange {
	
	public static void arrangeFileList(File sourceFolder, List<File> allFileList) throws IOException, InterruptedException {
		
		getAllFile(sourceFolder, allFileList);
		compareFileList(allFileList);
	}
	
    public static void getAllFile(File fileInput, List<File> allFileList) {
        
        File[] fileList = fileInput.listFiles();
        assert fileList != null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                getAllFile(file, allFileList);
            } else {             
                allFileList.add(file);
            }
        }
        
    }
    
    public static void compareFileList(List<File> allFileList) {
        Collections.sort(allFileList, new Comparator< File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });
    }
    
}
