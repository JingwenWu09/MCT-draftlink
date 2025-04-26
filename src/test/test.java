package test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AssrtFilter.RunAssert;
public class test {
	public static void main(String[] args) throws IOException {
//		File destFolder = new File("/home/elowen/桌面/MCTesingTestSuite/Mutation/dataflow/ConstantFolding");
////		Mutation.genMutation(sourceFolder, destFolder);
////		System.out.println( "ConstantFolding: " + Mutation.getFolderCFileCount(destFolder) );
//		List<File> failedFile = new ArrayList<File>();
//		System.out.println(failedFile.size());
//		File destAssertFolder = new File(destFolder.getAbsolutePath()+ "/assertFailed");
//		if(destAssertFolder.exists() == false || destAssertFolder.isDirectory() == false) destAssertFolder.mkdirs();
//		System.out.println(destAssertFolder.getAbsolutePath());
		String str = "a ... shfiougs_12";
		String regexRange = "^[^'\"]+.*\\s*\\.\\.\\.\\s*.*[^'\"]$";
		Pattern pRange = Pattern.compile(regexRange);
		Matcher mRange = pRange.matcher(str);
		if(mRange.find()) {
			String s = mRange.group();
			String[] split = s.split("\\s*\\.\\.\\.\\s*");
			System.out.println("left=" + split[0] + ";;;");
			System.out.println("right=" + split[1]+";;;");
		}
//		String[] operators = {"!", "==", "!=", ">=", "<=", ">", "<", "&&", "\\|\\|"};
//		List<Integer> op_index = new ArrayList<Integer>();
//		String regex = operators[0] + "[_0-9a-zA-Z\\s\\*\\(]+";
//		String line = "x - i >= 0 & y - i <= 0";
//		Pattern p = Pattern.compile(regex);
//		Matcher m = p.matcher(line);
//		while(m.find()) {
//			op_index.add(0);
//		}
//		System.out.println();
//		for(int i = 1; i < operators.length; i++) {
//			regex = "[_0-9a-zA-Z\\s\\*\\()]+" + operators[i] + "[_0-9a-zA-Z\\s\\*\\(]+";
//			p = Pattern.compile(regex);
//			m = p.matcher(line);
//			System.out.println(i);
//			while(m.find()) {
//				op_index.add(i);
//			}
//		}
//			System.out.println(op_index);
//		
	}
}
