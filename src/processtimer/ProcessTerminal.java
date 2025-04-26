package processtimer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessTerminal {

	public List<String> testProcessThread(String command, int second) throws IOException, InterruptedException{
		
		String[] cmd = new String[] { "/bin/bash", "-c", command };
		ProcessBuilder builder = new ProcessBuilder(cmd);
		builder.redirectErrorStream(true);
		Process proc = builder.start();
		
		ProcessWorker pw = new ProcessWorker(proc);
		pw.start();
		ProcessStatus ps = pw.getPs();
		try {
			pw.join(second * 1000);
			if(ps.exitCode == ps.CODE_STARTED) {
				pw.interrupt();
				List<String> result = new ArrayList<String>();
//				result.add("timeout");
				return result;
			}
			else {
				return ps.output;
			}
		}catch(InterruptedException e) {
			pw.interrupt();
			throw e;
		}finally {
			proc.destroy();
		}
	}
	
}
