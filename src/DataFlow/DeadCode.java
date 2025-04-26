package DataFlow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_Information.FileFormat;
import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.model.AstVariable;
import AST_Information.model.FunctionBlock;
import Filter.CopyFile;
import processtimer.ProcessStatus;
import processtimer.ProcessWorker;

public class DeadCode {
	static final String[] varTypes = {
			"signed char", "char", "unsigned char",
			"signed short int", "signed short", "short int", "short", "unsigned short int", "unsigned short",
			"signed int", "int", "unsigned int", 
			"signed long int", "signed long", "long int", "long", "unsigned long int", "unsigned long",
			"signed long long int", "signed long long", "long long int", "long long", "unsigned long long int", "unsigned long long",
			"float", "double"};
	static final List<String> operators = Arrays.asList("+","-","*","/",">>","<<","pow", "%");
	
	AstInform_Gen astgen;
	List<String> initialList = new ArrayList<String>();
	int mutationCnt = 0;
	File file;
	String mutationDir;
	FunctionBlock mainFunction = null;
	static String rename_suffix = "_1";

	public void Transformation(File file, String mutationDir) throws IOException, InterruptedException  {
//		File file = FileFormat.UnifiedBrace(filepath);
		mutationCnt = 0;
		this.file = file;
		this.mutationDir = mutationDir;
		initialList = FileFormat.genInitialList(file);
		astgen = new AstInform_Gen();
		astgen.getAstInform(file.getAbsolutePath());
		if(astgen.astList.size() == 0) return ;
		
		for(FunctionBlock functionBlock: astgen.getAllFunctionBlocks()) {
			if(functionBlock.name.equals("main")) {
				mainFunction = functionBlock;
				break ;
			}
		}
		//destFolder/filename/  目录下有 filename.c filename_0_unusedSrc.c filename_0_unusedMut.c filename_0.c filename_1_DStoreSrc.c  filename_1_DStoreMut.c filename_1.c
		//分别是：源文件、unusedVar加入printf之后、unusedVar的变体、unusedVar的source和变体的输出文件、deadStore加入printf之后、deadStore的变体、deadStore的source和变体的输出文件、
		UnusedVarTransformation();
		DeadStoreTransformation();
		
		System.out.println("mutationCnt = " + mutationCnt);
	}
	
	public void UnusedVarTransformation() throws IOException, InterruptedException  {
		Map<Integer, String> updatedLineMap = new HashMap<Integer, String>();
		Map<Integer, List<String>> addedPrintfLineMap = null;
		List<AstVariable> existVars = new ArrayList<AstVariable>();
		for(AstVariable var: astgen.allVarsMap.values()) {
			var.existUseLine.addAll(var.useLine);
		}
		
		String regexSpace = "^(\\s*)";
		Pattern pSpace = Pattern.compile(regexSpace);
		Matcher mSpace;
		String space = "";
		
		for(AstVariable var: astgen.allVarsMap.values()) {
			if(var.getIsParmVar() == true || var.getIsUsed() == true) {	//未使用，且不是参数
				existVars.add(var);
				continue ;
			}
			int row = var.getDeclareLine();
			String line = "";
			String newDeclareLine = "";
			if(updatedLineMap.containsKey(row)) line = updatedLineMap.get(row);
			else line = initialList.get(row);
			mSpace = pSpace.matcher(line);
			if(mSpace.find()) {
				space = mSpace.group(1);
			}
			
			if(astgen.lineDeclMap.get(row).size() == 1) {
			//该行只声明了var一个变量，删除该行;
			//若赋了初值，且右边出现其他变量(右边都是数字)，将右边的操作单独作为一个语句
				if(var.getIsIntialized()) {
					int index = line.indexOf("=")+1;
					if(Pattern.matches(".*[_a-zA-Z]+.*", line.substring(index)) ) {	//int a = ++b; => ++b;
						newDeclareLine = space + line.substring(index+1).replaceFirst("\\s*", "");
					}
				}
			}else {
			//该行声明了多个变量，判断声明的位置，从声明处截断
				int varStmtStart = 0, varStmtEnd = 0;
				String varRightExp = "";
				String regexFindVar = "[^_a-zA-Z0-9]" + var.getName() + "[^_a-zA-Z0-9]";	//前后各多截一个字符	int a = ++d,b,c; 中b的 ",b,"
				Pattern pFindVar = Pattern.compile(regexFindVar);
				Matcher mFindVar = pFindVar.matcher(line);
				if(mFindVar.find()) {
					varStmtStart = mFindVar.start()+1;
				}
				
				//1.leftpart
				
				int indexOp1 = line.substring(0, varStmtStart).lastIndexOf(",");
				int indexOp2 = line.substring(0, varStmtStart).lastIndexOf(";");
				if(indexOp1 != -1 || indexOp2 != -1) {
				//e.g.若a,b, e是，则 int a = ++d, e, b, c; =>消除a后为 ++d; int e, b, c; 中的e, b
					newDeclareLine = line.substring( 0, Math.max(indexOp1, indexOp2) ) + ";";	//b是,  e是;
				}//else: e.g.若a,e是，则 int a = 4, e, b, c; =>消除a后为int e, b, c; 中的e
				
				//2.varStmt
				//varEnd截到varStmt的标点 e.g. int b= c+3;中b的";"   e.g. int a = ++d,b,c; 中a、b的"," 以及c的";"
				varStmtEnd = line.indexOf(",", varStmtStart);	//int a = ++d,b,c; 中的 a、b
				if(varStmtEnd == -1) {
					varStmtEnd = line.indexOf(";", varStmtStart);	//int a = ++d,b,c; 中的 c
					if(varStmtEnd == -1) varStmtEnd = line.length();
				}
				
				if(var.getIsIntialized() == true) {
					varRightExp = line.substring(varStmtStart, varStmtEnd);	//int a = ++d,b,c; 中a的 "++d"
					varRightExp = varRightExp.substring(varRightExp.indexOf("=") + 1);
					if(Pattern.matches(".*[_a-zA-Z]+.*", varRightExp) == true) {
						if(newDeclareLine.equals("") == false) newDeclareLine += "\n";
						newDeclareLine += space + varRightExp.replaceFirst("^\\s*", "") + ";";
					}
				}
				
				//3.rightpart
				if(varStmtEnd != line.length() && line.charAt(varStmtEnd) == ',') {
					if(newDeclareLine.equals("") == false) newDeclareLine += "\n";
					String declare_type = var.getType();
					for(String otherVarId: astgen.lineDeclMap.get(row)) {
						AstVariable otherVar = astgen.allVarsMap.get(otherVarId);
						if(otherVar.getName().equals(var.getName())) continue;
						String other_type = otherVar.getType();
						if(otherVar.getKind().equals("common")) {
							declare_type = otherVar.getType();
						}else {
							int indexStar = other_type.indexOf("*");
							int indexBrace = other_type.indexOf("[");
							if(indexStar == -1) declare_type = other_type.substring(0,indexBrace);
							else if(indexBrace == -1) declare_type = other_type.substring(0, indexStar);
							else declare_type = other_type.substring(0, Math.min(indexStar, indexBrace));
						}
						break ;
					}
					newDeclareLine += space + declare_type + " " + line.substring(varStmtEnd+1).replaceFirst("^\\s*", "");
				}
			}
			if(newDeclareLine.matches("\\s*")) newDeclareLine = space + ";";
			updatedLineMap.put(row, newDeclareLine);
//			System.out.println("after:\n" + newDeclareLine +"\n");
		}
		
		if(updatedLineMap.size() == 0) return ;

		addedPrintfLineMap = genPrintfLineMap(existVars); //只考虑还存在的变量中满足varTypes的变量
		//文件中没有其他变量供比较，则在main函数首行随机生成一个全局变量并prinf
		String newVarDeclare = "";
		if(addedPrintfLineMap.size() == 0) {
			if(mainFunction == null) return ;
			String var_name = VariableOperation.genRandomName();
			String var_type = VariableOperation.genRandomType(varTypes);
			newVarDeclare = var_type + " " + var_name + " = " + VariableOperation.genRandomValue(var_type) + ";";
			updatedLineMap.put(mainFunction.startline, updatedLineMap.getOrDefault(mainFunction.startline, initialList.get(mainFunction.startline)) + "\n" + newVarDeclare);
			String addedPrintfLine = "printf(\"value_" + var_name + "=" 
					+ this.genPrintFormat(var_type) + "\\n\", " + var_name + ");";
			if(mainFunction.returnLineSet != null) {
				for(int returnRow: mainFunction.returnLineSet) {
					mSpace = pSpace.matcher(initialList.get(returnRow-1));
					if(mSpace.find()) space = mSpace.group(1);
					List<String> addedPrintfLineList = addedPrintfLineMap.getOrDefault(returnRow-1, new ArrayList<String>());
					addedPrintfLineList.add(space + addedPrintfLine);
					addedPrintfLineMap.put(returnRow-1, addedPrintfLineList);
				}
			}else {
				addedPrintfLineMap.put(mainFunction.endline-1, new ArrayList<String>(Arrays.asList("\t" + addedPrintfLine)) );
			}
		}
		String sourceFilepath = genSourceFile("unusedSrc", newVarDeclare, mainFunction.startline, addedPrintfLineMap);
		String mutationFilepath = genMutation("unusedMut", updatedLineMap, addedPrintfLineMap);
		List<String> sourcePrintf = null;
		List<String> mutationPrintf = null;
		File cmpBefore;
		File cmp;
		String commandCompile;
		
		File sourceFile = new File(sourceFilepath);
		cmpBefore = new File(sourceFile.getParent() + "/a.out");
		if(cmpBefore.exists()) cmpBefore.delete();
    	commandCompile = "cd " + sourceFile.getParent() + " && gcc -g " + sourceFile.getName() + " -lm";
    	testProcessThread(commandCompile);
    	cmp = new File(sourceFile.getParent() + "/a.out");
    	if(cmp.exists() == false) {
    		return ;
    	}else {
    		String commandRun = "cd " + sourceFile.getParent() + " && ./a.out";
    		sourcePrintf = testProcessThread(commandRun);
    		if(sourcePrintf.size() == 1 && sourcePrintf.get(0).trim().equals("timeout")) {
    			return ;
    		}
    	}
		if(cmp.exists()) cmp.delete();
    	
		File mutationFile = new File(mutationFilepath);
		cmpBefore = new File(mutationFile.getParent() + "/a.out");
		if(cmpBefore.exists()) cmpBefore.delete();
    	commandCompile = "cd " + mutationFile.getParent() + " && gcc -g " + mutationFile.getName() + " -lm";
    	testProcessThread(commandCompile);
    	cmp = new File(mutationFile.getParent() + "/a.out");
    	if(cmp.exists() == false) {
    		return ;
    	}else {
    		String commandRun = "cd " + mutationFile.getParent() + " && ./a.out";
    		mutationPrintf = testProcessThread(commandRun);
    		if(mutationPrintf.size() == 1 && mutationPrintf.get(0).trim().equals("timeout")) {
    			return ;
    		}
    		cmp.delete();
    	}
    	if(cmp.exists()) cmp.delete();
    	
//    	System.out.println("mutationCnt = " + mutationCnt);
//    	System.out.println("src:" + sourcePrintf.size() + "; " + sourceFile.getAbsolutePath());
//    	System.out.println("mut:" + mutationPrintf.size() + "; " + mutationFile.getAbsolutePath());
//    	for(String s: sourcePrintf) {
//    		System.out.println(s);
//    	}
//    	System.out.println();
//    	for(String s: mutationPrintf) {
//    		System.out.println(s);
//    	}
//    	System.out.println();
    	
    	if(sourcePrintf.size() != mutationPrintf.size()) return ;
    	if(sourcePrintf.size() == 0) return ;
	
		genPrintfCompareFile(sourcePrintf, mutationPrintf);
		mutationCnt ++;
	}
	
	public void DeadStoreTransformation( ) throws IOException, InterruptedException {
		DeadStore_Gen deadStore = new DeadStore_Gen();
		deadStore.getDeadStoreInform(file.getAbsolutePath());
		for(AstVariable var: astgen.allVarsMap.values()) {
			var.existUseLine.addAll(var.useLine);
		}
		
		Map<Integer, String> updatedLineMap = new HashMap<Integer, String>();
		Map<Integer, List<String>> addedPrintfLineMap = null;
		List<AstVariable> existVars = new ArrayList<AstVariable>();
		existVars.addAll(astgen.allVarsMap.values());
		
		String line = "";
		String space = "";
		String regexSpace = "^(\\s*)";
		Pattern pSpace = Pattern.compile(regexSpace);
		Matcher mSpace;
		//System.out.println( scanBuild.lineDeadStoreMap.size());
		
		for(List<DeadStoreVar> list: deadStore.lineDeadStoreMap.values()) {
			for(DeadStoreVar var: list) {
				String newline = "";
				int row = var.getRow();
				if(updatedLineMap.containsKey(row) ) line = updatedLineMap.get(row);
				else line = initialList.get(row);
//				System.out.println("before:\n" + line +"\n");
				mSpace = pSpace.matcher(line);
				if(mSpace.find()) {
					space = mSpace.group(1);
				}
//				if(astgen.lineDeclMap.containsKey(row)) {
//				//是声明行，查找是否是declareVar
//					List<String> lineDeclVarId = astgen.lineDeclMap.get(row);
//					for(String id: lineDeclVarId) {
//						AstVariable astVar = astgen.allVarsMap.get(id);
//						if(astVar.getName().equals(var.getName())) {
//							String updatedLine = DeadDeclareElimination(row, line, lineDeclVarId.size(), astVar);
//							existVars.remove(astVar);
//						
//							/*tip:删除declare时的deadStore，但在之后有使用，所以添加声明行*/
//							/*但是发现deadStore不检查声明行，所以不存在declareLine的deadStore消除*/
//							if(astVar.useLine.size() == 0) {
//								existVars.remove(astVar);
//							} else {
//								mSpace = pSpace.matcher(updatedLine);
//								if(mSpace.find()) {
//									space = mSpace.group(1);
//								}
//								if(updatedLine.matches(".*;.*")) {
//									updatedLine = space + astVar.getType() + " " + astVar.getName();
//								}else {
//									updatedLine += "\n" + space + astVar.getType() + " " + astVar.getName();
//								}
//							}
//								
//							updatedLineMap.put(row, updatedLine);
//							break ;
//						}
//					}
//				}else {
					List<String> lineUseVarId = astgen.lineUseMap.getOrDefault(row, new ArrayList<String>());
					if(lineUseVarId.size() == 0) continue ;
					for(String id: lineUseVarId) {
						AstVariable astVar = astgen.allVarsMap.get(id);
						if(astVar.getName().equals(var.getName())) {
							astVar.existUseLine.remove(row);
						}
					}
					int varStoreStart = 0, varStoreEnd = 0, varStmtLeftStart = 0;
					String varRightExp = "", varLeftExp = "";
					String regexFindVar = "[^_a-zA-Z0-9]" + var.getName() + "[^_a-zA-Z0-9]";	//前后各多截一个字符	int a = ++d,b,c; 中b的 ",b,"
					Pattern pFindVar = Pattern.compile(regexFindVar);
					Matcher mFindVar = pFindVar.matcher(line);
					if(mFindVar.find()) {
						varStoreStart = mFindVar.start()+1;
					}
					
					//1.leftpart
					int indexOp1 = line.substring(0, varStoreStart).lastIndexOf(",");
					int indexOp2 = line.substring(0, varStoreStart).lastIndexOf(";");
					if(indexOp1 != -1 || indexOp2 != -1) {
					//e.g. a = 3, q = p = 4; 中q、p前的,是max(op1, op2);
						varStmtLeftStart = Math.max(indexOp1, indexOp2)+1;
						newline = line.substring( 0, varStmtLeftStart-1) + ";";
					}					
					//System.out.println("leftpart" + newline + "\n");
					
					//2.varStmt
					//e.g. a = 3, q = p = 4; 中p的varLeftExp = " q = ", q的varLeftExp = " ", a的varLeftExp = ""
					varLeftExp = line.substring(varStmtLeftStart, varStoreStart);
					if(varLeftExp.lastIndexOf("=") != -1) {
						varLeftExp = varLeftExp.substring(0, varLeftExp.lastIndexOf("=")+1);
						//截到=号
					}
					varStoreEnd = line.indexOf(",", varStoreStart);
					if(varStoreEnd == -1) {
						varStoreEnd = line.indexOf(";", varStoreStart);
						if(varStoreEnd == -1) {
							varStoreEnd = line.length();
						}
					}
					varRightExp = line.substring(varStoreStart, varStoreEnd);	
					varRightExp = varRightExp.substring(varRightExp.indexOf("=") + 1);
					if(Pattern.matches(".*[^0-9]+.*=", varLeftExp) == true) {
					//hasLeft等号 leftExp = rightExp;
						//if( (a = j = i + 1) < 0) 中的 j ,varStoreEnd = line.length-1，即")"
						if(newline.equals("") == false && varStoreEnd != line.length()) newline += "\n";
						newline += space + varLeftExp.replaceFirst("^\\s*", "").replaceFirst("\\s*$", "") + varRightExp;
						if(varStoreEnd != line.length()) {
							if(line.charAt(varStoreEnd) == ',' || line.charAt(varStoreEnd) == ';') {
								newline += ";";
							}
						}
						
					}else {
					//leftExp没有赋值=
						if(varStoreEnd == line.length()) newline += varLeftExp + varRightExp;
						else if(Pattern.matches(".*[^0-9\\s]+.*", varRightExp) == true) {
							if(newline.equals("") == false) newline += "\n";
							newline += space + varRightExp.replaceFirst("^\\s*", "") + ";";
						}
					}
					//3.rightpart
					if(varStoreEnd != line.length() && line.charAt(varStoreEnd) == ',') {
						if(newline.equals("") == false) newline += "\n";
						newline += space + line.substring(varStoreEnd+1).replaceFirst("^\\s*", "");
					}
//				}
				if(newline.matches("\\s*")) newline = space + ";";
				updatedLineMap.put(row, newline);
//				System.out.println("after:\n" + newline + "\n");
			}
		}
		if(updatedLineMap.size() == 0) return ;
		
		addedPrintfLineMap = genPrintfLineMap(existVars);
		String newVarDeclare = "";
		if(addedPrintfLineMap.size() == 0) {
			if(mainFunction == null) return ;
			String var_name = VariableOperation.genRandomName();
			String var_type = VariableOperation.genRandomType(varTypes);
			newVarDeclare = var_type + " " + var_name + " = " + VariableOperation.genRandomValue(var_type) + ";";
			updatedLineMap.put(mainFunction.startline, updatedLineMap.getOrDefault(mainFunction.startline, initialList.get(mainFunction.startline)) + "\n" + newVarDeclare);
			String addedPrintfLine = "printf(\"value_" + var_name + "=" 
					+ this.genPrintFormat(var_type) + "\\n\", " + var_name + ");";
			if(mainFunction.returnLineSet != null) {
				/*tip:顺序？？*/
				for(int returnRow: mainFunction.returnLineSet) {
					mSpace = pSpace.matcher(initialList.get(returnRow-1));
					if(mSpace.find()) space = mSpace.group(1);
					List<String> addedPrintfLineList = addedPrintfLineMap.getOrDefault(returnRow-1, new ArrayList<String>());
					addedPrintfLineList.add(space + addedPrintfLine);
					addedPrintfLineMap.put(returnRow-1, addedPrintfLineList);
				}
			}else {
				addedPrintfLineMap.put(mainFunction.endline-1, new ArrayList<String>(Arrays.asList("\t" + addedPrintfLine)) );
			}
		}
		String sourceFilepath = genSourceFile("DStoreSrc", newVarDeclare, mainFunction.startline, addedPrintfLineMap);
		String mutationFilepath = genMutation("DStoreMut", updatedLineMap, addedPrintfLineMap);
		List<String> sourcePrintf = null;
		List<String> mutationPrintf = null;
		File cmpBefore;
		File cmp;
		String commandCompile;
		
		File sourceFile = new File(sourceFilepath);
		cmpBefore = new File(sourceFile.getParent() + "/a.out");
		if(cmpBefore.exists()) cmpBefore.delete();
    	commandCompile = "cd " + sourceFile.getParent() + " && gcc -g " + sourceFile.getName() + " -lm";
    	testProcessThread(commandCompile);
    	cmp = new File(sourceFile.getParent() + "/a.out");
    	if(cmp.exists() == false) {
    		return ;
    	}else {
    		String commandRun = "cd " + sourceFile.getParent() + " && ./a.out";
    		sourcePrintf = testProcessThread(commandRun);
    		if(sourcePrintf.size() == 1 && sourcePrintf.get(0).trim().equals("timeout")) {
    			return ;
    		}
    	}
    	if(cmp.exists()) cmp.delete();
    	
    	File mutationFile = new File(mutationFilepath);
		cmpBefore = new File(mutationFile.getParent() + "/a.out");
		if(cmpBefore.exists()) cmpBefore.delete();
    	commandCompile = "cd " + mutationFile.getParent() + " && gcc -g " + mutationFile.getName() + " -lm";
    	testProcessThread(commandCompile);
    	cmp = new File(mutationFile.getParent() + "/a.out");
    	if(cmp.exists() == false) {
    		return ;
    	}else {
    		String commandRun = "cd " + mutationFile.getParent() + " && ./a.out";
    		mutationPrintf = testProcessThread(commandRun);
    		if(mutationPrintf.size() == 1 && mutationPrintf.get(0).trim().equals("timeout")) {
    			return ;
    		}
    	}
    	if(cmp.exists()) cmp.delete();
    	
//    	System.out.println("mutationCnt = " + mutationCnt);
//    	System.out.println("src:" + sourcePrintf.size() + "; " + sourceFile.getAbsolutePath());
//    	System.out.println("mut:" + mutationPrintf.size() + "; " + mutationFile.getAbsolutePath());
//    	for(String s: sourcePrintf) {
//    		System.out.println(s);
//    	}
//    	System.out.println();
//    	for(String s: mutationPrintf) {
//    		System.out.println(s);
//    	}
//    	System.out.println();
    	
    	if(sourcePrintf.size() != mutationPrintf.size()) return ;
    	if(sourcePrintf.size() == 0) return ;
		genPrintfCompareFile(sourcePrintf, mutationPrintf);
		mutationCnt ++;
	}
	
	public Map<Integer, List<String>> genPrintfLineMap(List<AstVariable> existVars) {
		Map<Integer, List<String>> addedPrintfLineMap = new HashMap<Integer, List<String>>();
		String space = "";
		String regexSpace = "^(\\s*)";
		Pattern pSpace = Pattern.compile(regexSpace);
		Matcher mSpace;
		for(AstVariable var: astgen.allVarsMap.values()) {
			if(!existVars.contains(var)) continue ;
			for(String type: varTypes) {
				if(var.getType().equals(type)) {
					String addedPrintLine = "printf(\"value_" + var.getName() + "=" + this.genPrintFormat(type) + "\\n\", " + var.getName() + ");";
					for(int row: var.existUseLine ) {
						String existline = initialList.get(row);
						if(existline.trim().startsWith("switch") == true) continue ;
						mSpace = pSpace.matcher(existline);
						if(mSpace.find()) {
							space = mSpace.group(1);
						}
						List<String> addedPrintfLine = addedPrintfLineMap.getOrDefault(row, new ArrayList<String>());
						addedPrintfLine.add(space + addedPrintLine);
						addedPrintfLineMap.put(row, addedPrintfLine);
					}
					break ;
				}
			}
		}
		
//		for(int row: addedPrintfLineMap.keySet()) {
//			System.out.println("row: " + row + "\n" + addedPrintfLineMap.get(row));
//		}
		return addedPrintfLineMap;
	}
	
	private String genPrintFormat(String type) {
		String format = "";
		if(type.contains("unsigned ")) format = "u";
		else format = "d";
		if(type.contains("char")) {
			format = "%hh" + format;
		}else if(type.contains("short")) {
			format = "%h" + format;
		}else if(type.contains("long long")) {
			format = "%ll" + format;
		}else if(type.contains("long")){
			format = "%l" + format;
		}else if(type.contains("float")) {
			format = "%f";
		}else if(type.contains("double")) {
			format = "%lf";
		}else if(type.contains("int")){ //int
			format = "%" + format;
		}
		
		return format;
	}
	
	private String genSourceFile(String suffix, String newVarDeclare, int mainFuncStartLine, Map<Integer, List<String>> addedPrintfLineMap) {
		String filename = file.getName();
		File newFile = new File(mutationDir + "/" + filename.substring(0, filename.lastIndexOf(".c")) + "_" + mutationCnt + "_" + suffix + ".c");
		
		try {
			//System.out.println(dir);
			if(newFile.exists()) {
				newFile.delete();
			}
			newFile.createNewFile();
			FileWriter fw = new FileWriter(newFile, true);
			PrintWriter pw = new PrintWriter(fw);
			
//			System.out.println("outmostSline: " +trans.outmostSline);
//			System.out.println(initialList.get(trans.outmostSline));
			
			int lineCnt = -1;
			for(String line: initialList) {
				lineCnt++;
				if(lineCnt == 0) continue;
				
				pw.println(line);
				if(lineCnt == 1 && newVarDeclare.equals("") == false ) pw.println(newVarDeclare);
				if(addedPrintfLineMap.containsKey(lineCnt)) {
					for(String newPrintf: addedPrintfLineMap.get(lineCnt))
						pw.println(newPrintf);
				}
			}
			
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
		}catch (Exception e) {
			e.printStackTrace();
		} 
		
		return newFile.getAbsolutePath();
	}
	
	private String genMutation(String suffix, Map<Integer, String> updatedLineMap, Map<Integer, List<String>> addedPrintfLineMap) {
		
		String filename = file.getName();
		File newFile = new File(mutationDir + "/" + filename.substring(0, filename.lastIndexOf(".c")) + "_" + mutationCnt + "_" + suffix + ".c");
		try {
			//System.out.println(dir);
			if(newFile.exists()) {
				newFile.delete();
			}
			newFile.createNewFile();
			FileWriter fw = new FileWriter(newFile, true);
			PrintWriter pw = new PrintWriter(fw);
			
//			System.out.println("outmostSline: " +trans.outmostSline);
//			System.out.println(initialList.get(trans.outmostSline));
			
			int lineCnt = -1;
			for(String line: initialList) {
				lineCnt++;
				if(lineCnt == 0) continue;
				if(updatedLineMap.containsKey(lineCnt)) {
					pw.println(updatedLineMap.get(lineCnt));
					if(addedPrintfLineMap.containsKey(lineCnt)) {
						for(String newPrintf: addedPrintfLineMap.get(lineCnt))
							pw.println(newPrintf);
					}
					continue ;
				}
				pw.println(line);
				
				if(addedPrintfLineMap.containsKey(lineCnt)) {
//					System.out.println("lineCnt: " + lineCnt);
					for(String newPrinf: addedPrintfLineMap.get(lineCnt))
						pw.println(newPrinf);
				}
			}
			
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		} 
		
		return newFile.getAbsolutePath();
	}
	
	public String genPrintfCompareFile(List<String> sourcePrintf, List<String> mutationPrintf) {
		try {
			String filename = file.getName();
			File newFile = new File(mutationDir + "/" + filename.substring(0, filename.lastIndexOf(".c")) + "_" + mutationCnt + ".c");
			System.out.println("");
			
			if(newFile.exists()) {
				newFile.delete();
			}
			newFile.createNewFile();
			FileWriter fw = new FileWriter(newFile, true);
			PrintWriter pw = new PrintWriter(fw);
			
			pw.println("#include <assert.h>");
			pw.println("#include <stdio.h>");
			pw.println("#include <string.h>");
			pw.println("");
			pw.println("int main(){");
			int strCnt = 1;
			
			for(int i = 0; i < sourcePrintf.size(); i++) {
				String line1 = sourcePrintf.get(i).replaceFirst("\\s*$", "");
				System.out.println(i+ " " + line1);
				if(line1.startsWith("value_") == false) continue ;
				String line2 = mutationPrintf.get(i).replaceFirst("\\s*$", "");
				System.out.println(i+ " " + line1);
				int len = line1.length();
				pw.println("\t" + "char str_source_" + strCnt + "[] = " + "\"" + line1 + "\";");
				pw.println("\t" + "char str_mutation_" + strCnt + "[] = " + "\"" + line2 + "\";");
				pw.println("\tfor(int i = 0; i < " + len + "; i++) {");
				pw.println("\t\tassert(str_source_" + strCnt + "[i]" 
						+ " == str_mutation_" + strCnt + "[i]) ;");
				pw.println("\t}");
				strCnt++;
			}
			pw.println("\treturn 0;");
			pw.println("}");
			
			
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
		}catch (Exception e) {
			e.printStackTrace();
		} 
		
		return "";
	}
	
		
	public static List<String> testProcessThread(String command) throws IOException, InterruptedException{
		
		String[] cmd = new String[] { "/bin/bash", "-c", command };
		ProcessBuilder builder = new ProcessBuilder(cmd);
		builder.redirectErrorStream(true);
		Process proc = builder.start();
		
		ProcessWorker pw = new ProcessWorker(proc);
		pw.start();
		ProcessStatus ps = pw.getPs();
		try {
			pw.join(5 * 1000);
			if(ps.exitCode == ps.CODE_STARTED) {
				pw.interrupt();
				List<String> result = new ArrayList<String>();
				result.add("timeout");
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
