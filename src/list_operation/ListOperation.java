package list_operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListOperation {
	
	public static List<List<String>> randomList(List<List<String>> llist, int total){
		List<List<String>> randomList = new ArrayList<List<String>>();
		int size = llist.size();
		Random random =  new Random();
		for(int i=0; i<total; i++) {
			int ran = random.nextInt(size);
			randomList.add(llist.get(ran));
		}
		return randomList;

	}
	
	public static void printList(List<String> list) {
		for(String l: list) {
			System.out.println(l);
		}
	}
	
	public static List<String> getListPart(List<String> list, int startLine, int endLine){
		List<String> partList = new ArrayList<String>();
		for(int i=1; i<=list.size(); i++) {
			if(i>=startLine && i<=endLine) {
				partList.add(list.get(i-1));
			}
		}
		return partList;
	}
	
	public static List<String> genInitialList(String filepath) {
		List<String> initialList = new ArrayList<String>();
		File file = new File(filepath);
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
			e1.printStackTrace();
		}
		
		return initialList;
	}
}
