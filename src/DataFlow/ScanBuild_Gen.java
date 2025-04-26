package DataFlow;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanBuild_Gen {
	
	public String filepath;
	public List<String> scanBuildList = new ArrayList<String>();
	//file行号，deadstore的变量名
	public Map<Integer, ArrayList<DeadStoreVar>> lineDeadStoreMap = new HashMap<Integer, ArrayList<DeadStoreVar>>();
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*scan-build -o TEST gcc /home/jing/MCTesting/dataflow/00007.c 
		scan-build: Using '/usr/bin/clang+llvm-10.0.0/bin/clang-10' for static analysis
		/home/jing/MCTesting/dataflow/00007.c:9:2: warning: Value stored to 'x' is never read
		 * */
		
		//warning: Although the value stored to 'p' is used in the enclosing expression, the value is never actually read from 'p'
	}
	
	public void getScanBuildInform(String filepath) {
		this.filepath = filepath; 
		String command = "scan-build -o /home/jing/MCTesting/ScanBuild gcc " + filepath;
		String[] cmd = new String[] { "/bin/sh", "-c", command };

		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			builder.redirectErrorStream(true);
			Process proc = builder.start();
			//proc.waitFor();
			
			InputStream ins = proc.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
			
			String regexDeadStore1 = filepath + ":([0-9]+):([0-9]+):\\swarning:\\sValue\\sstored\\sto\\s'([_a-zA-Z]+[_a-zA-Z0-9]*)'\\sis\\snever\\sread\\s*";
			String regexDeadStore2 = filepath + 
					":([0-9]+):([0-9]+):\\swarning:\\s.*?the\\svalue\\sis\\snever\\sactually\\sread\\sfrom\\s'([_a-zA-Z]+[_a-zA-Z0-9]*)'\\s*";
			Pattern pDeadStore1 = Pattern.compile(regexDeadStore1);
			Pattern pDeadStore2 = Pattern.compile(regexDeadStore2);
			Matcher mDeadStore;
			
			String line = null;
			while((line = br.readLine()) != null) {
				scanBuildList.add(line);
				mDeadStore = pDeadStore1.matcher(line);
				if(mDeadStore.find()) {
					int row = Integer.parseInt(mDeadStore.group(1));
					int col = Integer.parseInt(mDeadStore.group(2));
					String name = mDeadStore.group(3);
					DeadStoreVar newVar = new DeadStoreVar(name, row, col);
					System.out.println("Find deadStore: " + name + " " + row + " " + col);
					if(lineDeadStoreMap.containsKey(row)) {
						lineDeadStoreMap.get(row).add(newVar);
					}else {
						ArrayList<DeadStoreVar> lineVars = new ArrayList<DeadStoreVar>();
						lineVars.add(newVar);
						lineDeadStoreMap.put(row, lineVars);
					}
				}else {
					mDeadStore = pDeadStore2.matcher(line);
					if(mDeadStore.find()) {
						int row = Integer.parseInt(mDeadStore.group(1));
						int col = Integer.parseInt(mDeadStore.group(2));
						String name = mDeadStore.group(3);
						DeadStoreVar newVar = new DeadStoreVar(name, row, col);
						//System.out.println("find " + name + " " + row + " " + col);
						if(lineDeadStoreMap.containsKey(row)) {
							lineDeadStoreMap.get(row).add(newVar);
						}else {
							ArrayList<DeadStoreVar> lineVars = new ArrayList<DeadStoreVar>();
							lineVars.add(newVar);
							lineDeadStoreMap.put(row, lineVars);
						}
					}
				}
			}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}
