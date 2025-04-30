package tools;

import fileoperation.FileDelete;
import fileoperation.SeahornFormat;
import fileoperation.getAllFileList;
import processtimer.ProcessTerminal;

import java.io.*;
import java.util.*;

public class TestSeahorn {
	List<String> successful = new ArrayList<String>();
	List<String> failure = new ArrayList<String>();
	List<String> other = new ArrayList<String>();
	List<String> timeout = new ArrayList<String>();

	boolean isHaveFalse = false;

	Map<String, String> fileResult = new TreeMap<String, String>();

	public boolean testTools(String dir) throws IOException, InterruptedException {
		File folder = new File(dir);
		if(!folder.exists() || !folder.isDirectory()) {
			System.out.println(dir + " not exists or is not a directory");
			return true;
		}

		File seaOutput = new File(dir + "/seahorn.txt");
		if(seaOutput.exists()) {
			return checkSat(seaOutput);
		}

		File outputsFile = new File(dir + "/output");
		if(outputsFile.exists()) {
			FileDelete.deleteFolder(outputsFile);
		}

		List<File> allFileList = new ArrayList<File>();

		getAllFileList getFileList = new getAllFileList(folder);
		getFileList.getAllFile(folder, allFileList);
		getFileList.compareFileList(allFileList);

		for(File file: allFileList){
			if(!file.getName().endsWith(".c")) {
				continue;
			}
			if(file.getName().endsWith("_seahorn.c")) {
				file.delete();
				continue;
			}
			fileResult.put(file.getName(), "non-execution");
		}

		File seaPart = new File(dir + "/seahorn-part.txt");
		if(!seaPart.exists()){
			seaPart.createNewFile();
		}else{
			readSeahornPartFile(seaPart);
		}

		for(String key: fileResult.keySet()) {
			if(!fileResult.get(key).equals("non-execution")){
				continue;
			}
			File file = new File(dir + "/" + key);
			System.out.println(key);
			SeahornFormat sf = new SeahornFormat();
			File seaFile = sf.transFormat(file);
			
//			String partPath = seaFile.getParent().replace(Main.dockerDir, "/host");
//			String command = "cd " + Main.dockerDir + " && docker exec " + Main.dockerName + " bash -c \"cd " + partPath
//					+ " && sea pf -m64 " + seaFile.getName() + "\"";
			
			String command = "cd " + seaFile.getParent() + " && sea bpf -m64 --bmc=opsem " + seaFile.getName();
			
			ProcessTerminal pt = new ProcessTerminal();
			List<String> execLines = pt.testProcessThread(command, 15);
			pt.killSeahornThread();

//			for(String s: execLines) {
//				System.out.println(s);
//			}

			String seaResult = "";
			if(execLines.size() == 0) {
				//如果第一个原始文件就是timeout，那么认为其余所有都是timeout
//				if(key.equals(folder.getName() + ".c")){
//					for(String innerFiles: fileResult.keySet()){
//						fileResult.put(innerFiles, "timeout");
//					}
//					seaFile.delete();
//					break;
//				}
				fileResult.put(key, "timeout");
				rwSeahornPartTxt(seaPart, key, "timeout");
				System.out.println("timeout.........");
				seaFile.delete();

				continue;
			}
//			if(execLines.get(execLines.size()-1).trim().equals("unsat") ||
//					execLines.get(execLines.size()-1).contains("WARNING: no assertion was found so either program does not have assertions or front-end discharged them.")) {
			if(execLines.get(execLines.size()-1).trim().equals("unsat")){
				seaResult = "unsat";
				System.out.println("unsat.........");
			}
			else if(execLines.get(execLines.size()-1).trim().equals("sat")){
				seaResult = "sat";
				isHaveFalse = true;
				System.out.println("find sat.........");
			}
			else {
				seaResult = "other condition";
				System.out.println("other condition.........");
			}

			seaFile.delete();
			fileResult.put(key, seaResult);
			rwSeahornPartTxt(seaPart, key, seaResult);
			
		}

		classifyResult();
		
		rwSeahornTxt(dir, folder.getName());

		seaPart.delete();

		ProcessTerminal pt = new ProcessTerminal();
		pt.printMemInfo();

		if(isHaveFalse) {
			return false;
		}
		
		return true;
	}

	public void rwSeahornPartTxt(File seaPart, String filename, String result) {
		try {
			FileWriter fw = new FileWriter(seaPart, true);
			PrintWriter pw = new PrintWriter(fw);

			pw.println(filename + ": " + result);

			pw.flush();
			fw.flush();
			pw.close();
			fw.close();

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void classifyResult(){
		for(String key: fileResult.keySet()){
			String result = fileResult.get(key);
			if(result.equals("unsat")){
				successful.add(key);
			}
			else if(result.equals("sat")){
				failure.add(key);
			}
			else if(result.equals("timeout")){
				timeout.add(key);
			}
			else if(result.equals("other condition")){
				other.add(key);
			}
		}
	}

	public void rwSeahornTxt(String dir, String filename) {
		try {
			File file = new File(dir + "/seahorn.txt");
			
			if(file.exists()) {
				file.delete();
			}
			file.createNewFile();
			
			FileWriter fw = new FileWriter(file, true);
			PrintWriter pw = new PrintWriter(fw);
				
			pw.println(filename + ".c SeaHorn Model Checker Result: \n");
			pw.println("\n-----------------------------------------------------------\n");

			successful.forEach( s -> {
				pw.println(s + ": unsat");
			});
			
			pw.println("\n-----------------------------------------------------------\n");
			failure.forEach( s -> {
				pw.println(s + ": sat");
			});
			
			pw.println("\n-----------------------------------------------------------\n");
			timeout.forEach( s -> {
				pw.println(s + ": timeout");
			});
			
			pw.println("\n-----------------------------------------------------------\n");
			other.forEach( s -> {
				pw.println(s + ": other condition");
			});
			
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public boolean checkSat(File file) {
		try {
			//read
			InputStreamReader in = new InputStreamReader(new FileInputStream(file));
			BufferedReader bf = new BufferedReader(in);

			String line = "";
			while((line = bf.readLine()) != null) {
				if(line.trim().contains(": sat")) {
					return false;
				}
			}
			bf.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return true; 
	}

	public void readSeahornPartFile(File file) {
		try {
			//read
			InputStreamReader in = new InputStreamReader(new FileInputStream(file));
			BufferedReader bf = new BufferedReader(in);

			String line = "";
			while((line = bf.readLine()) != null) {
				if(line.trim().matches(".+:\\s{1}.+")) {
					if(line.trim().contains("sat") && !isHaveFalse){
						isHaveFalse = true;
					}
					int index = line.trim().indexOf(":");
					fileResult.put(line.trim().substring(0, index), line.trim().substring(index + 1).trim());
				}
			}
			bf.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkAllTimeout(File file) {
		try {
			//read
			InputStreamReader in = new InputStreamReader(new FileInputStream(file));
			BufferedReader bf = new BufferedReader(in);

			String line = "";
			while((line = bf.readLine()) != null) {
				if(line.trim().contains(": sat") || line.trim().contains(": unsat")
						|| line.trim().contains(": other condition")) {
					return false;
				}
			}
			bf.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
