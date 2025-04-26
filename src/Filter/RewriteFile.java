package Filter;

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

public class RewriteFile{
	
	public static void main(String[] args) {
//		String filepath = "/home/elowen/桌面/builtin-object-size-3.c";
		File folder = new File("/home/jing/桌面/IfRedone/pr103181");
		
		if(!folder.exists()) {
			System.out.println("folder not exist!");
			return ;
		}
		
		for(File file: folder.listFiles()) {
			rewrite(file);
		}
		System.out.println("finished!");
	}
	
	public static void rewrite(File file) {
		
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
			
			for(String line: initialList) {
				if(line.trim().equals("assert(x == x_1) ;")) {
					pw.println("\tfor(int i = 0; i < sizeof(x)/sizeof(x[0]); i++) {");
					pw.println("\t\tassert(x[i] == x_1[i]) ;");
					pw.println("\t}");
					continue ;
				}
//				if(line.trim().equals("assert(e == e_1) ;")) {
//					pw.println("\tfor(int i = 0; i < sizeof(e)/sizeof(e[0]); i++) {");
//					pw.println("\t\tassert(e[i] == e_1[i]) ;");
//					pw.println("\t}");
//					continue ;
//				}
//				if(line.trim().equals("assert(u.x == u_1.x) ;")) {
//					pw.println("\tfor(int i = 0; i < sizeof(u.x)/sizeof(u.x[0]); i++) {");
//					pw.println("\t\tassert(u.x[i] == u_1.x[i]) ;");
//					pw.println("\t}");
//					continue ;
//				}
//				if(line.trim().equals("assert(u.z == u_1.z) ;")) {
//					pw.println("\tfor(int i = 0; i < sizeof(u.z)/sizeof(u.z[0]); i++) {");
//					pw.println("\t\tassert(u.z[i] == u_1.z[i]) ;");
//					pw.println("\t}");
//					continue ;
//				}
//				if(line.trim().equals("assert(v.x == v_1.x) ;")) {
//					pw.println("\tfor(int i = 0; i < sizeof(v.x)/sizeof(v.x[0]); i++) {");
//					pw.println("\t\tassert(v.x[i] == v_1.x[i]) ;");
//					pw.println("\t}");
//					continue ;
//				}
//				String r = "assert\\((x[123]) == x[123]_1\\)\\s;";
//				Pattern p = Pattern.compile(r);
//				Matcher m = p.matcher(line.trim());
//				if(m.find()) {
//					String str = m.group(1);
//					pw.println("\tfor(int i = 0; i < sizeof(" + str + ")/sizeof(" + str + "[0]); i++) {");
//					pw.println("\t\tassert(" + str + "[i] == " + str + "_1[i]) ;");
//					pw.println("\t}");
//					continue ;
//				}
			
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
