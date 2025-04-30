package fileoperation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteFabs {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File sourceFolder = new File("/home/elowen/桌面/mct-result-loop/Unrolling-BeforeDeleteFabs");
		File destFolder = new File("/home/elowen/桌面/mct-result-loop/Unrolling");
		rewriteWitoutFabs(sourceFolder, destFolder);
		System.out.println("end!");
	}

	public static void rewriteWitoutFabs(File sourceFolder, File destFolder) {
		
		if(sourceFolder.exists() == false) {
			System.out.println("sourceFolder not exist!");
			return ;
		}
		
		if(destFolder.exists() == false) {
			System.out.println("destFolder not exist!");
			return ;
		}
		
		int fileCnt = 0;

		for(File subFolder: sourceFolder.listFiles()) {
			if(subFolder.isDirectory() == false) continue ;
//			File falseFolder = new File(subFolder.getAbsolutePath() + "/false");
//			if(falseFolder.exists() == false) {
//				continue;
//			}
//			falseFolderCnt ++;
			for(File file: subFolder.listFiles()) {
				if(file.getName().endsWith(".c") == false) continue ;
				RewriteFile(file, new File(destFolder.getAbsolutePath() + "/" + subFolder.getName()));
				fileCnt ++;
			}
		}
		
		System.out.println("fileCnt = " + fileCnt);
	}
	public static void RewriteFile(File file, File destFolder) {
		try {
			//System.out.println(dir);
			
			if(destFolder.exists() == false) destFolder.mkdirs(); 
			File newFile = new File(destFolder.getAbsolutePath() + "/" + file.getName());
			List<String> falseFileList = genList(file);
			
			FileWriter fw = new FileWriter(newFile, true);
			PrintWriter pw = new PrintWriter(fw);
			
//			System.out.println("outmostSline: " +trans.outmostSline);
//			System.out.println(initialList.get(trans.outmostSline));
			
			for(String line: falseFileList) {
				pw.println(line);
			}
			
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
		}catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	public static List<String> genList(File file) {
		List<String> initialList = new ArrayList<String>();
		InputStreamReader ins;
		BufferedReader br = null;
		String regex = "(.*)assert\\(\\s*\\bfabs\\b\\((.*)-([^>].*)\\)\\s*<\\s*0\\.000001\\s*\\).*;";
		Pattern p = Pattern.compile(regex);
		Matcher m;
		
		try {
			ins = new InputStreamReader(new FileInputStream(file));
			br = new BufferedReader(ins);
			String line = null;
			while((line = br.readLine()) != null) {
				m = p.matcher(line);
				if(m.find()) {
					line = m.group(1) + "assert(" + m.group(2) + "==" + m.group(3) + ") ;";
				}
				initialList.add(line);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return initialList;
	}
}
