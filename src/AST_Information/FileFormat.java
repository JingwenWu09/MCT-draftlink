package AST_Information;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.Inform_Gen.LoopInform_Gen;
import AST_Information.model.AstVariable;
import AST_Information.model.LoopStatement;

public class FileFormat {
	
	public static List<String> genInitialList(File file) {
		List<String> initialList = new ArrayList<String>();
		InputStreamReader ins;
		BufferedReader br = null;
		try {
			ins = new InputStreamReader(new FileInputStream(file));
			br = new BufferedReader(ins);
			String line = null;
			while((line = br.readLine()) != null) {
				if(initialList.size() == 0) {
					initialList.add(null);
					initialList.add(line);
				}else initialList.add(line);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return initialList;
	}
	
}
