package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import fileoperation.FileDelete;
import fileoperation.getAllFileList;
import processtimer.ProcessTerminal;

public class TestCBMC {
	
	List<String> successful = new ArrayList<String>();
	List<String> failure = new ArrayList<String>();
	List<String> other = new ArrayList<String>();
	List<String> timeout = new ArrayList<String>();

	public Map<String, Boolean> allResult = new HashMap<String, Boolean>();

	String dir = "";
	File folder;

	public void testTools(String dir) throws IOException, InterruptedException {
		this.dir = dir;
		this.folder = new File(dir);
//		String commandProp = "--bounds-check --pointer-check --memory-leak-check --div-by-zero-check --signed-overflow-check --unsigned-overflow-check --pointer-overflow-check --conversion-check --undefined-shift-check --pointer-primitive-check";
		if(!folder.exists() || !folder.isDirectory()) {
			System.out.println(dir + " not exists or is not a directory");
			return;
		}


		testConfig("assertions");
		testConfig("bounds-check");
		testConfig("pointer-check");
//		testConfig("memory-leak-check");
//		testConfig("div-by-zero-check");
//		testConfig("signed-overflow-check");
//		testConfig("unsigned-overflow-check");
//		testConfig("pointer-overflow-check");
//		testConfig("conversion-check");
//		testConfig("undefined-shift-check");
//		testConfig("pointer-primitive-check");

	}

	public void testConfig(String property) throws IOException, InterruptedException {
		successful = new ArrayList<String>();
		failure = new ArrayList<String>();
		other = new ArrayList<String>();
		timeout = new ArrayList<String>();
//		//String commandSmt = "-z3";
		System.out.println("start " + property + "..........");
		File cbmcOutput = new File(dir + "/cbmc_" + property + ".txt");
		if(cbmcOutput.exists()) {
			checkFailure(property, cbmcOutput);
			return;
		}
		String config = " --" + property + " --no-assertions";
		if(property.equals("assertions")){
			config = "";
		}
		List<File> allFileList = new ArrayList<File>();

		getAllFileList getFileList = new getAllFileList(folder);
		getFileList.getAllFile(folder, allFileList);
		getFileList.compareFileList(allFileList);

		for(File file: allFileList) {
			if(!file.getName().endsWith(".c")) {
				continue;
			}

			boolean isHaveUnwind = false;

			String command = "cd " + file.getParent() + " && " + "cbmc " + file.getName()  + config;
			ProcessTerminal pt = new ProcessTerminal();
			List<String> execLines = pt.testProcessThread(command, 6);
			if(execLines.size() == 0) {
				isHaveUnwind = true;
				ProcessTerminal ptt = new ProcessTerminal();
				execLines = ptt.testProcessThread("cd " + file.getParent() + " && " + "cbmc " + file.getName() + config + " --unwind 10", 6);
				if(execLines.size() == 0) {
					timeout.add(file.getName());
					continue;
				}
			}
			if(execLines.get(execLines.size()-1).contains("VERIFICATION SUCCESSFUL")) {
				successful.add(file.getName() + (isHaveUnwind? "  --unwind": ""));
			}
			else if(execLines.get(execLines.size()-1).contains("VERIFICATION FAILED")){
				System.out.println("find");
				failure.add(file.getName() + (isHaveUnwind? "  --unwind": ""));
			}
			else {
				other.add(file.getName() + (isHaveUnwind? "  --unwind": ""));
			}
		}

		if(failure.size() != 0){
			allResult.put(property, false);
		}
		else{
			allResult.put(property, true);
		}

		rwCBMCTxt(property);
	}
	
	public void rwCBMCTxt(String property) {
		try {
			File file = new File(dir + "/cbmc_" + property + ".txt");
			
			if(file.exists()) {
				file.delete();
			}
			file.createNewFile();
			
			FileWriter fw = new FileWriter(file, true);
			PrintWriter pw = new PrintWriter(fw);
				
			pw.println(folder.getName() + ".c CBMC Model Checker Result: \n");
			pw.println("\n-----------------------------------------------------------\n");
			successful.forEach( s -> {
				pw.println(s + ": successful");
			});
			
			pw.println("\n-----------------------------------------------------------\n");
			failure.forEach( s -> {
				pw.println(s + ": failed");
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
	
	public void checkFailure(String property, File file) {
		try {
			//read
			InputStreamReader in = new InputStreamReader(new FileInputStream(file));
			BufferedReader bf = new BufferedReader(in);
			
			String line = "";
			while((line = bf.readLine()) != null) {
				if(line.trim().contains(": failed")) {
					allResult.put(property, false);
					return;
				}
			}
			allResult.put(property, true);
			bf.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
