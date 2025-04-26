package AssrtFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.HashSet;

public class Label{
	
	public static void main(String[] args) {
//		String filepath = "/home/elowen/桌面/builtin-object-size-3.c";
		File file = new File(""
				+ "/home/jing/Desktop//IVElimination/use-after-scope-switch-3_dc_0.c");
		deleteLabel(file);
		System.out.println("finished!");
	}
	
	public static void deleteLabel(File file) {
		
		try {
			//System.out.println(dir);
			List<String> initialList = genInitialList(file);
			File tempFile = File.createTempFile("temp", ".c", file.getParentFile());
			
			if(tempFile.exists()) {
				tempFile.delete();
			}
			tempFile.createNewFile();
			FileWriter fw = new FileWriter(tempFile, true);
			PrintWriter pw = new PrintWriter(fw);
			
			Set<String> labelName = new HashSet<String>();
			String regexLabel = "([_a-zA-Z][_a-zA-Z0-9]*)\\s*:";
			Pattern pLabel = Pattern.compile(regexLabel);
			Matcher mLabel;
			
			for(String line: initialList) {
				mLabel = pLabel.matcher(line.trim());
				if(mLabel.find()) {
					String label = mLabel.group(1);
					if(labelName.contains(label)) {
						continue ;
					}else labelName.add(label);
				}
				
				pw.println(line);
				
			}
			
			file.delete();
			
			tempFile.renameTo(file);
			
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
		}catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	public static List<String> genInitialList(File file) {
		List<String> initialList = new ArrayList<String>();
		InputStreamReader ins;
		BufferedReader br = null;
		try {
			ins = new InputStreamReader(new FileInputStream(file));
			br = new BufferedReader(ins);
			String line = null;
			while((line = br.readLine()) != null) {
				initialList.add(line);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return initialList;
	}
	
	
}
