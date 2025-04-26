package AST_Information.Inform_Gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_Information.model.*;
import processtimer.ProcessStatus;
import processtimer.ProcessWorker;

public class AstInform_Gen {
	
	public String filepath;
	public Map<String, AstVariable> allVarsMap = new LinkedHashMap<String, AstVariable>();
	public Map<String, StructUnionBlock> allStructUnionMap = new HashMap<String, StructUnionBlock>();	//<id, block>
	public Map<String, FieldVar> allFieldVarsMap = new HashMap<String, FieldVar>();
	public Map<TypedefDecl, String> typedefDeclMap = new HashMap<TypedefDecl, String>(); //<typedefDecl, Recordid>
	
	public List<String> astList = new ArrayList<String>();
	//file行号，使用的变量id
	public Map<Integer, ArrayList<String>> lineUseMap = new HashMap<Integer, ArrayList<String>>();
	//file行号，定义的变量id
	public Map<Integer, ArrayList<String>> lineDeclMap = new HashMap<Integer, ArrayList<String>>();
	public Map<Integer, ArrayList<EqualOperatorBlock>> equalOpMap = new HashMap<Integer, ArrayList<EqualOperatorBlock>>();
	public List<EqualOperatorBlock> equalOpBlocks = new ArrayList<EqualOperatorBlock>();
	public Map<String, VarDeclareBlock> varDeclBlockMap = new HashMap<String, VarDeclareBlock>();	//<varid, vardeclareBlcok>
	public List<VarDeclareBlock> varDeclBlocks = new ArrayList<VarDeclareBlock>();
	private boolean isStart = false;
	
	
	public void getAstInform(String filepath) throws NullPointerException {
		this.filepath = filepath; 
		//this.globalVarsSet = getGlobalVarsSet(filepath);
		String command = "clang -fsyntax-only -Xclang -ast-dump " + filepath + " -w -Xanalyzer -analyzer-disable-all-checking";
//		String[] cmd = new String[] { "/bin/bash", "-c", command };
		
		try {
//			ProcessBuilder builder = new ProcessBuilder(cmd);
//			builder.redirectErrorStream(true);
//			Process proc = builder.start();
//			//proc.waitFor();
//			
//			InputStream ins = proc.getInputStream();
//			BufferedReader br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
			List<String> execLines = testProcessThread(command);
//			System.out.println(":::" + execLines.size());
			if(execLines.size() == 1 && execLines.get(0).trim().equals("timeout")) return ;
//			String regexStartLine1 = "<.*>\\sline:([0-9]+)(:[0-9]+)";
//			//String regexStartLine2 = ".*[a-zA-Z]+\\s0x[a-z0-9]+\\s<" + filepath + ".*:([0-9]+):[0-9]+(,\\s.*)?>.*"; 
//			String regexStartLine2 = ".*[a-zA-Z]+\\s0x[a-z0-9]+\\s<" + filepath + ".*?:([0-9]+):[0-9]+(,\\s.*)?>.*"; 
//			String regexStartLine3 = "<line:([0-9]+)(:[0-9]+)";
			String regexStartLine1 = "<line:([0-9]+)(:[0-9]+)";
			String regexStartLine2 = ".*[a-zA-Z]+\\s0x[a-z0-9]+\\s<" + filepath + ".*?:([0-9]+):[0-9]+(,\\s.*)?>.*"; 
			String regexStartLine3 = "<.*>\\sline:([0-9]+)(:[0-9]+)";
			String regexEndLine = "line:([0-9]+)(:[0-9]+>)";
			Pattern pStartLine1= Pattern.compile(regexStartLine1);
			Pattern pStartLine2 = Pattern.compile(regexStartLine2);
			Pattern pStartLine3 = Pattern.compile(regexStartLine3);
			Pattern pEndLine = Pattern.compile(regexEndLine);
			Matcher mStartLine1, mStartLine2, mStartLine3, mEndLine;
			
			String regexVarUse = "\\s(Parm)?Var\\s(0x[0-9a-z]+)\\s'([_a-zA-Z]+[_a-zA-Z0-9]*)'\\s'([_a-zA-Z]+[_a-zA-Z0-9\\s\\(\\)\\*\\[\\]]*)'";	
//			String regexVarDecl = "(Parm)?VarDecl\\s(0x[0-9a-z]+)\\s<.*>.*\\s([_a-zA-Z]+[_a-zA-Z0-9]*)\\s'([_a-zA-Z]+[_a-zA-Z0-9\\s\\(\\)\\*\\[\\]]*)'(\\s)?(cinit)?"; 
//			String regexVarDecl = "(Parm)?VarDecl\\s(0x[0-9a-z]+)\\s<.*>.*\\s([_a-zA-Z]+[_a-zA-Z0-9]*)\\s'([_a-zA-Z]+[_a-zA-Z0-9,\\.\\s\\(\\)\\*\\[\\]]*)'(\\s)?(cinit)?"; 
//			String regexVarDecl = "(Parm)?VarDecl\\s(0x[0-9a-z]+)\\s<.*>.*?(\\bused\\b)?\\s([_a-zA-Z]+[_a-zA-Z0-9]*)\\s'([_a-zA-Z]+[_a-zA-Z0-9,\\.\\s\\(\\)\\*\\[\\]]*)'"; 
			String regexVarDecl = "(Parm)?VarDecl\\s(0x[0-9a-z]+)\\s(.*prev\\s0x[0-9a-z]+\\s)?<.*>.*?(\\bused\\b)?\\s([_a-zA-Z]+[_a-zA-Z0-9]*)\\s'([_a-zA-Z]+[_a-zA-Z0-9,\\.\\s\\(\\)\\*\\[\\]]*)'"; 
//			String regexVaList = "(Parm)?VarDecl\\s(0x[0-9a-z]+)\\s<.*>.*?(\\bused\\b)?\\s([_a-zA-Z]+[_a-zA-Z0-9]*)\\s'(va_list)':'.*'(\\s)?(cinit)?"; 
			Pattern pVarUse = Pattern.compile(regexVarUse);
			Pattern pVarDecl = Pattern.compile(regexVarDecl);
//			Pattern pVaList = Pattern.compile(regexVaList);
			Matcher mVarUse, mVarDecl;
//			Matcher mVaList;
			
			String regexEqualOp1 = ".*AssignOperator\\s(0x[a-z0-9]+)\\s<.*>\\s'([_a-zA-Z]+[_a-zA-Z0-9\\s\\(\\)\\*\\[\\]]*)'\\s'.=";
			String regexEqualOp2 = ".*BinaryOperator\\s(0x[a-z0-9]+)\\s<.*>\\s'([_a-zA-Z]+[_a-zA-Z0-9\\s\\(\\)\\*\\[\\]]*)'\\s'=";
			Pattern pEqualOp1 = Pattern.compile(regexEqualOp1);
			Pattern pEqualOp2 = Pattern.compile(regexEqualOp2);
			Matcher mEqualOp1, mEqualOp2;
			
			Stack<EqualOperatorBlock> equalOpStack = new Stack<EqualOperatorBlock>();
			Stack<VarDeclareBlock> varDeclStack = new Stack<VarDeclareBlock>();
			EqualOperatorBlock topEqualOpBlock, popEqualOpBlock;
			VarDeclareBlock topVarDeclBlock, popVarDeclBlock;
			
			String regexStructUnionDecl = "\\bRecordDecl\\s(0x[a-z0-9]+)(\\s[a-z]+\\s0x[0-9a-z]+)?\\s<.*>.*\\s(\\bstruct\\b)?(\\bunion\\b)?\\s([_a-zA-Z]+[_a-zA-Z0-9]*)"; //definition
			String regexFieldDecl = "\\bFieldDecl\\s(0x[a-z0-9]+)\\s<.*>.*?(\\breferenced\\b)?\\s([_a-zA-Z]+[_a-zA-Z0-9]*)\\s'([_a-zA-Z]+[_a-zA-Z0-9,\\.\\s\\(\\)\\*\\[\\]]*)'"; 
			Pattern pStructUnionDecl = Pattern.compile(regexStructUnionDecl);
			Pattern pFieldDecl = Pattern.compile(regexFieldDecl);
			Matcher mStructUnionDecl, mFieldDecl;
			Stack<StructUnionBlock> suBlockStack = new Stack<StructUnionBlock>();
			StructUnionBlock topSUBlock, popSUBlock;
			
			String regexTypedefDecl = "\\bTypedefDecl\\s(0x[a-z0-9]+)\\s<.*>.*\\s(\\breferenced\\b)?\\s([_a-zA-Z]+[_a-zA-Z0-9]*)\\s'([_a-zA-Z]+[_a-zA-Z0-9]*)'";
			String regexRecordType = "\\bRecordType\\s(0x[a-z0-9]+)\\s'([_a-zA-Z]+[_a-zA-Z0-9]*)'";
			String regexcRecordId = "\\bRecord\\s(0x[a-z0-9]+)\\s'([_a-zA-Z]+[_a-zA-Z0-9]*)'";
			Pattern pTypedefDecl  = Pattern.compile(regexTypedefDecl);
			Pattern pRecordType = Pattern.compile(regexRecordType);
			Pattern pRecordId = Pattern.compile(regexcRecordId);
			Matcher mTypedefDecl, mRecordType, mRecordId;
			Stack<TypedefDecl> topDeclStack = new Stack<TypedefDecl>();
			TypedefDecl tpDecl = null;
			
			int startLine = 0, endLine = 0, tempLine = 0;
			int astTempLine = 0;
			int topEndLine = 0;
			AstVariable newVar = null;
			AstVariable useVar = null;
			EqualOperatorBlock equalOpBlock;
			VarDeclareBlock varDeclBlock;
//			String line = null;
			
			int Cnt = 0;
			//一、遍历ast所有行
//			while((line = br.readLine()) != null) {System.out.println(line);
			for(String line: execLines) {
				if(this.isStart == false) {
					if( isAstStart(line, filepath) == false) {
						continue;
					}
				}
//				System.out.println(astList.size());
//				System.out.println("before:\n" + line);
				genAstList(line);
				astTempLine = astList.size();
				line = astList.get(astTempLine-1);
//				System.out.println("after:\n" + line);
//				System.out.println(astList.size());
//				System.out.println();
				String deleteChars = "([0-9]+\\s[(warning(s)?)(error(s)?)]+\\s)(and\\s[0-9]+\\s[(warning(s)?)(error(s)?)]+\\s)*(generated.)";
				Pattern p = Pattern.compile(deleteChars);
				Matcher m = p.matcher(line);
				if(m.find()) {
					continue;
				}
				
				//1.update tempLine
				mStartLine1 = pStartLine1.matcher(line);
				mStartLine2 = pStartLine2.matcher(line);
				mStartLine3 = pStartLine3.matcher(line);
				if(mStartLine1.find()) {
					tempLine = Integer.parseInt(mStartLine1.group(1));
				}else if(mStartLine2.find()) {
					tempLine = Integer.parseInt(mStartLine2.group(1));
				}else if(mStartLine3.find()) {
					tempLine = Integer.parseInt(mStartLine3.group(1));
				}
				startLine = tempLine;
				mEndLine = pEndLine.matcher(line);
				if(mEndLine.find()) {
					endLine = Integer.parseInt(mEndLine.group(1));
				}else {
					endLine = tempLine;
				}
				
				//if(tempLine <= 26) System.out.println("line" + tempLine + ":\n" + line +"\n");
				//2.1 pop出VarDecl
				//用来临时存放pop出的vardecl，为了使同行的vardecl按照声明的先后顺序放入vardeclblockList
				//如 int a = 1, c = a; 先pop出c = a，再pop出a = 1，则按照原顺序放入tempList
				List<VarDeclareBlock> tempVarDeclList = new ArrayList<VarDeclareBlock>();
				int declAstEndLine = astTempLine-1;
				while(!varDeclStack.isEmpty()) {
					topVarDeclBlock = varDeclStack.peek();
					topEndLine = topVarDeclBlock.getEndLine();
					if(tempLine <= topEndLine) {	
						break;
					}else {
						popVarDeclBlock = varDeclStack.pop();
						popVarDeclBlock.setAstEndLine(declAstEndLine);	
						tempVarDeclList.add(popVarDeclBlock);
						if(!varDeclStack.isEmpty()) {
							//并列声明的情况，先声明的astEndLine是后声明的astStartLine-1
							declAstEndLine = varDeclStack.peek().getAstStartLine()-1;
						}
					}
				}
				if(!tempVarDeclList.isEmpty()) varDeclBlocks.addAll(tempVarDeclList);
				
				//2.2 同理，pop出equalOpBlock
				List<EqualOperatorBlock> tempEuqalOpList = new ArrayList<EqualOperatorBlock>();
				int equalAstEndLine = astTempLine-1;
				while(!equalOpStack.isEmpty()) {
					topEqualOpBlock = equalOpStack.peek();
					topEndLine = topEqualOpBlock.getEndLine();
					if(tempLine <= topEndLine) {	
						break;
					}else {
						popEqualOpBlock = equalOpStack.pop();
						popEqualOpBlock.setAstEndLine(equalAstEndLine);	
						tempEuqalOpList.add(popEqualOpBlock);
						if(!equalOpStack.isEmpty()) {
							equalAstEndLine = equalOpStack.peek().getAstStartLine()-1;
						}
					}
				}
				if(!tempEuqalOpList.isEmpty()) equalOpBlocks.addAll(tempEuqalOpList);
				
				//2.3 同理 StructUnionBlock
				int suAstEndLine = astTempLine-1;
				while(!suBlockStack.isEmpty()) {
					topSUBlock = suBlockStack.peek();
					topEndLine = topSUBlock.getEndLine();
					if(tempLine <= topEndLine) {	
						break;
					}else {
						popSUBlock = suBlockStack.pop();
						popSUBlock.setAstEndLine(suAstEndLine);	
						if(!suBlockStack.isEmpty()) {
							suAstEndLine = suBlockStack.peek().getAstStartLine()-1;
						}
					}
				}
				
				
				//3.1 newVar and varDeclBlock
				mVarDecl = pVarDecl.matcher(line);
				if(mVarDecl.find()) {
					String var_isParmVarDecl = mVarDecl.group(1);
					String varid = mVarDecl.group(2);
					String var_isUsed = mVarDecl.group(3);
					String varname = mVarDecl.group(4);
					String vartype = mVarDecl.group(5);
					String regexType2 = "'([_a-zA-Z]+[_a-zA-Z0-9,\\.\\s\\(\\)\\*\\[\\]]*)'";
					boolean isCinit = false;
					if(line.indexOf(" cinit", mVarDecl.end()) != -1) {
						isCinit = true;
					}
					if(vartype.equals("struct __va_list_tag *")) {
						vartype = "va_list";
					}
					if(vartype.equals("bool")) {
						vartype = "_Bool";
					}
					String varkind = AstVariable.getVarKindByType(vartype);
//					String var_isParmVarDecl = mVarDecl.group(1);
//					String varid = mVarDecl.group(2);
//					String varname = mVarDecl.group(3);
//					String vartype = mVarDecl.group(4);
//					String varcinit = mVarDecl.group(6);
					if(varkind.equals("pointer")) {
						newVar = new PointerVar(varid, varname, vartype);
					}else if(varkind.equals("array")) {
						//System.out.println("[array]:");
						//System.out.println("type: " + vartype);
						newVar = new ArrayVar(varid, varname, vartype);
					}else {
						newVar = new CommonVar(varid, varname, vartype);
					}
					newVar.setDeclareline(tempLine);
					newVar.setKind(varkind);
					
					if(var_isParmVarDecl != null) {
						newVar.setIsParmVar(true);
						newVar.setIsInitialized(true);
					}else {
						newVar.setIsInitialized(isCinit);
					}
					if(var_isUsed != null) {
						newVar.setIsUsed(true);
					}else {
						newVar.setIsUsed(false);
					}
					
					Pattern pType2 = Pattern.compile(regexType2);
					Matcher mType2 = pType2.matcher(line.substring(mVarDecl.end()));
					if(mType2.find()) {newVar.setType2(mType2.group(1));}
					
					//if(newVar.getKind().equals("array")) System.out.println(newVar.getDeclareLine() +"\n");
					allVarsMap.put(varid, newVar);	//(1)变量放入allVarsMap中
					//(2)将newvar放入fileList中的当前行
					if(lineDeclMap.containsKey(tempLine)) {
						lineDeclMap.get(tempLine).add(varid);
					}else {
						ArrayList<String> lineUseList = new ArrayList<String>();
						lineUseList.add(varid);
						lineDeclMap.put(tempLine, lineUseList);
					}
					
//					System.out.println(line);
//					System.out.println("newVar: " + newVar.getName() + " " + newVar.getId() + " " + newVar.getDeclareLine() + " " + newVar.getIsIntialized());
					
					//(3) 创建varDeclBlock，加入时astTempLine是block的astStartLine,astEndLine在pop的时候才知道
					varDeclBlock = new VarDeclareBlock(varid, startLine, endLine, astTempLine, 0);
					if(isCinit == true) {
						varDeclBlock.setIsCinit(true);
						//同理，创建equalOpBlock，加入时astTempLine是block的astStartLine,astEndLine在pop的时候才知道
						String equalId = varid, equalCalcuType = vartype;
						equalOpBlock = new EqualOperatorBlock(equalId, equalCalcuType, startLine, endLine, astTempLine, 0);
						equalOpBlock.setIsCinit(true);
						equalOpStack.push(equalOpBlock);
						/*tip: newAdd*/
						if(equalOpMap.containsKey(tempLine)) {
							equalOpMap.get(tempLine).add(equalOpBlock);
						}else {
							ArrayList<EqualOperatorBlock> lineEqualOpList = new ArrayList<EqualOperatorBlock>();
							lineEqualOpList.add(equalOpBlock);
							equalOpMap.put(tempLine, lineEqualOpList);
						}
					}
					varDeclStack.push(varDeclBlock);
					varDeclBlockMap.put(varid, varDeclBlock);
				}
				
				//3.2. new Equal
				mEqualOp1 = pEqualOp1.matcher(line);
				mEqualOp2 = pEqualOp2.matcher(line);
				if(mEqualOp1.find()) {
					//System.out.println(tempLine);
					String equalId = mEqualOp1.group(1);
					String equalCalcuType = mEqualOp1.group(2);
					equalOpBlock = new EqualOperatorBlock(equalId, equalCalcuType, startLine, endLine, astTempLine, 0);
					equalOpStack.push(equalOpBlock);
					/*tip: newAdd*/
					if(equalOpMap.containsKey(tempLine)) {
						equalOpMap.get(tempLine).add(equalOpBlock);
					}else {
						ArrayList<EqualOperatorBlock> lineEqualOpList = new ArrayList<EqualOperatorBlock>();
						lineEqualOpList.add(equalOpBlock);
						equalOpMap.put(tempLine, lineEqualOpList);
					}
				}else if(mEqualOp2.find()) {
					String equalId = mEqualOp2.group(1);
					String equalCalcuType = mEqualOp2.group(2);
					equalOpBlock = new EqualOperatorBlock(equalId, equalCalcuType, startLine, endLine, astTempLine, 0);
					equalOpStack.push(equalOpBlock);
					/*tip: newAdd*/
					if(equalOpMap.containsKey(tempLine)) {
						equalOpMap.get(tempLine).add(equalOpBlock);
					}else {
						ArrayList<EqualOperatorBlock> lineEqualOpList = new ArrayList<EqualOperatorBlock>();
						lineEqualOpList.add(equalOpBlock);
						equalOpMap.put(tempLine, lineEqualOpList);
					}
				}
				
				//4.1 new StructUnionBlock
				mStructUnionDecl = pStructUnionDecl.matcher(line);
				if(mStructUnionDecl.find()) {
					String id = mStructUnionDecl.group(1);
					String block_type = mStructUnionDecl.group(3);
					if(block_type == null) block_type = mStructUnionDecl.group(4);
					String name = mStructUnionDecl.group(5);
					StructUnionBlock newSUBlock = new StructUnionBlock(id, name, block_type);
					newSUBlock.setStartLine(startLine);
					newSUBlock.setEndLine(endLine);
					newSUBlock.setAstStartLine(astTempLine);
					if(!suBlockStack.isEmpty()) {
						topSUBlock = suBlockStack.peek();
						topSUBlock.getChildStructUnion().add(newSUBlock);
						newSUBlock.setParentStructUnion(topSUBlock);
					}
					suBlockStack.push(newSUBlock);
					allStructUnionMap.put(id, newSUBlock);
				}
				
				///4.2 new FieldVar
				mFieldDecl = pFieldDecl.matcher(line);
				if(mFieldDecl.find() && !suBlockStack.isEmpty()) {
					String id = mFieldDecl.group(1);
					String name = mFieldDecl.group(3);
					String type = mFieldDecl.group(4);
					FieldVar newFieldVar = new FieldVar(id, name, type);
					newFieldVar.setDeclareline(startLine);
					if(mFieldDecl.group(2) != null) {
						newFieldVar.setIsReferenced(true);
					}else {
						newFieldVar.setIsReferenced(false);
					}
					topSUBlock = suBlockStack.peek();
					topSUBlock.getChildField().add(newFieldVar);
					newFieldVar.setParentStructUnion(topSUBlock);
					allFieldVarsMap.put(id, newFieldVar);
				}
				
				//4.3 new TypedefDecl
				mTypedefDecl = pTypedefDecl.matcher(line);
				if(mTypedefDecl.find()) {
					String id = mTypedefDecl.group(1);
					String name = mTypedefDecl.group(3);
					String record_type = mTypedefDecl.group(4);
					tpDecl = new TypedefDecl(id, name, startLine);
					tpDecl.setReocrdType(record_type);
				}
				
				//4.4 new Rocord
				mRecordType = pRecordType.matcher(line);
				if(mRecordType.find() && tpDecl!=null) {
					tpDecl.setReocrdType(mRecordType.group(2));
				}
				mRecordId = pRecordId.matcher(line);
				if(mRecordId.find() && tpDecl != null) {
					tpDecl.setRecordId(mRecordId.group(1));
					tpDecl.setRecordName(mRecordId.group(2));
					this.typedefDeclMap.put(tpDecl, tpDecl.getRecordId());
					tpDecl = null;
				}
				
				//5. varUse
				mVarUse = pVarUse.matcher(line);
				if(mVarUse.find()) {
					String useVarid = mVarUse.group(2);
					useVar = allVarsMap.get(useVarid);
					useVar.useLine.add(tempLine);
					/*tip: */
					useVar.setIsUsed(true);
					if(lineUseMap.containsKey(tempLine)) {
						lineUseMap.get(tempLine).add(useVarid);
					}else {
						ArrayList<String> lineUseVars = new ArrayList<String>();
						lineUseVars.add(useVarid);
						lineUseMap.put(tempLine, lineUseVars);
					}
				}
				
			}
			
			//二，遍历完ast
			//1.在最后一行有声明的情况e.g { ....// lastFileLine:  int a, b; return 0;}
			//按声明顺序放入declBlocks
			for( int i = 0; i < varDeclStack.size(); i++) {
				int declAstEndLine;
				VarDeclareBlock tempVarDeclBlock = varDeclStack.get(i);
				if(i < varDeclStack.size()-1) {
					declAstEndLine = varDeclStack.get(i+1).getAstStartLine()-1;
				}else declAstEndLine = astTempLine;
				tempVarDeclBlock.setAstEndLine(declAstEndLine);	
				varDeclBlocks.add(tempVarDeclBlock);
			}
			varDeclStack.clear();
			
			genVarDeclBlockInform();
			
			//2.同理，考虑在最后一行有=的情况
			//按=顺序放入equalOpBlocks
			for( int i = 0; i < equalOpStack.size(); i++) {
				int equalAstEndLine;
				EqualOperatorBlock tempEqualOpBlock = equalOpStack.get(i);
				if( i < equalOpStack.size()-1) {
					equalAstEndLine = equalOpStack.get(i+1).getAstStartLine()-1;
				}else equalAstEndLine = astTempLine;
				tempEqualOpBlock.setAstEndLine(equalAstEndLine);	//当前astTempLine就是ast最后一行
				equalOpBlocks.add(tempEqualOpBlock);
			}
			equalOpStack.clear();
			
			genEblockInform();
			
			//System.out.println("last:" + astList.get(astList.size()-1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Set<String> getGlobalVarsSet(String filepath){
		Set<String> globalVarsSet = new HashSet<String>();
		String command = "clang -S -emit-llvm " + filepath + " -o temp.ll";
		String[] cmd = new String[] { "/bin/sh", "-c", command };
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			builder.redirectErrorStream(true);
			Process proc = builder.start();
			
			File file = new File(filepath.substring(0,filepath.lastIndexOf("/")+1) + "temp.ll");
			InputStreamReader ins = new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(ins);
			String line;
			boolean hasGlobalVar = false;
			while((line = br.readLine()) != null) {
				if(hasGlobalVar == false) {
					if(line.startsWith("@") == true) {
						globalVarsSet.add(line.substring(1, line.indexOf(" ")));
						hasGlobalVar = true;
					}else continue ;
				}else {
					if(line.startsWith("@") == true) {
						globalVarsSet.add(line.substring(1, line.indexOf(" ")));
					}else break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//proc.waitFor();
//		Iterator<String> it = globalVarsSet.iterator();
//		while(it.hasNext()) {
//			System.out.println("name:" + it.next() + ";");
//		}
		return globalVarsSet;
		
	}
	
	public List<AstVariable> getAllVars(){
		List<AstVariable> allVars = new ArrayList<AstVariable>();
		for(AstVariable var: allVarsMap.values()) 
			allVars.add(var);
		return allVars;
	}
	
	public Set<String> getGlobalVarsSet(Map<String, AstVariable> allVarsMap, List<FunctionBlock> allFunctions){
		Set<String> globalVarsSet = new HashSet<String>();
		for(AstVariable var:allVarsMap.values()) {
			//参数非globalVars
			if(var.getIsParmVar() == true) continue;
			//是否在函数内声明
			int declareline = var.getDeclareLine();
//			System.out.println("varname: " + var.getName());
//			System.out.println("declareline: " + declareline);
			boolean isGlobalVar = true;
			for(FunctionBlock fun: allFunctions) {
//				System.out.println("fun: " +fun.name + " " + fun.startline +" " + fun.endline);
				if(fun.startline <= declareline && declareline <= fun.endline) {
					isGlobalVar = false;
//					System.out.println("varname:" + var.getName());
					break ;
				}
			}
//			System.out.println();
			if(isGlobalVar == true) {
				globalVarsSet.add(var.getId());
				var.setIsGlobal(true);
			}
		}
		
		return globalVarsSet;
	}
	
	public List<FunctionBlock> getAllFunctionBlocks(){
		List<FunctionBlock> allBlocks = new ArrayList<FunctionBlock>();
		FunctionBlock block = null;
		String regexFunDecl1 = ".*\\bFunctionDecl\\s(0x[a-z0-9]+)\\s<.*>.*\\s([0-9a-zA-Z_]+)\\s'.*'(\\sextern){0,1}";
		String regexFunDecl2 = ".*\\bFunctionDecl\\s(0x[a-z0-9]+)\\sprev\\s0x[a-z0-9]+\\s<.*>.*\\s([0-9a-zA-Z_]+)\\s'.*'(\\sextern){0,1}";
		String regexCompStmt = ".*\\bCompoundStmt\\s.*";
		String regexReturnStmt = ".*\\bReturnStmt\\s.*";
		String regexStartLine1 = "<.*>\\sline:([0-9]+)(:[0-9]+)";
		//String regexStartLine2 = ".*[a-zA-Z]+\\s0x[a-z0-9]+\\s<" + filepath + ".*:([0-9]+):[0-9]+(,\\s.*)?>.*"; 
		String regexStartLine2 = ".*[a-zA-Z]+\\s0x[a-z0-9]+\\s<" + filepath + ".*?:([0-9]+):[0-9]+(,\\s.*)?>.*"; 
		String regexStartLine3 = "<line:([0-9]+)(:[0-9]+)";
		String regexEndLine = "line:([0-9]+)(:[0-9]+>)";
		Pattern pFunDecl1 = Pattern.compile(regexFunDecl1);
		Pattern pFunDecl2 = Pattern.compile(regexFunDecl2);
		Pattern pCompStmt = Pattern.compile(regexCompStmt);
		Pattern pReturnStmt = Pattern.compile(regexReturnStmt);
		Pattern pStartLine1= Pattern.compile(regexStartLine1);
		Pattern pStartLine2 = Pattern.compile(regexStartLine2);
		Pattern pStartLine3 = Pattern.compile(regexStartLine3);
		Pattern pEndLine = Pattern.compile(regexEndLine);
		Matcher mFunDecl1, mFunDecl2, mCompStmt, mReturnStmt;
		Matcher mStartLine1, mStartLine2, mStartLine3, mEndLine;
		int startline = 0, endline = 0, templine = 0;
		int functionEndLine = 0;	//记录当前function的ast是否遍历完
		boolean findCompStmt = false;
		for(String line: astList) {
			mStartLine1 = pStartLine1.matcher(line);
			mStartLine2 = pStartLine2.matcher(line);
			mStartLine3 = pStartLine3.matcher(line);
			if(mStartLine1.find()) {
				templine = Integer.parseInt(mStartLine1.group(1));
			}else if(mStartLine2.find()) {
				templine = Integer.parseInt(mStartLine2.group(1));
			}else if(mStartLine3.find()) {
				templine = Integer.parseInt(mStartLine3.group(1));
			}
			startline = templine;
			mEndLine = pEndLine.matcher(line);
			if(mEndLine.find()) {
				endline = Integer.parseInt(mEndLine.group(1));
			}else {
				endline = templine;
			}
			
			mFunDecl1 = pFunDecl1.matcher(line);
			mFunDecl2 = pFunDecl2.matcher(line);
			if(mFunDecl1.find()) {
				block = new FunctionBlock();
				block.id = mFunDecl1.group(1);
				block.name = mFunDecl1.group(2);
				block.startline = startline;
				block.endline = endline;
				if(mFunDecl1.group(3) != null) block.ifExtern = true;
				else block.ifExtern = false;
				findCompStmt = true;
				functionEndLine = endline;
				continue ;
			}else if(mFunDecl2.find()) {
				block = new FunctionBlock();
				block.id = mFunDecl2.group(1);
				block.name = mFunDecl2.group(2);
				block.startline = startline;
				block.endline = endline;
				if(mFunDecl2.group(3) != null) block.ifExtern = true;
				else block.ifExtern = false;
				findCompStmt = true;
				functionEndLine = endline;
				continue ;
			}
			if(functionEndLine < templine) {
				block = null;
				continue ;
			}
			
			mReturnStmt = pReturnStmt.matcher(line);
			if(mReturnStmt.find()) {
				if(block!=null) {
					if(block.returnLineSet == null) {
						block.returnLineSet = new TreeSet<Integer>();
					}
					block.returnLineSet.add(templine);
				}
			}
						
			if(findCompStmt){
				mCompStmt = pCompStmt.matcher(line);
				if(mCompStmt.find()) {
					block.leftBraceLine = startline;
					block.rightBraceLine = endline;
					allBlocks.add(block);
					findCompStmt = false;	//functionCompStmt已找到
				}else continue;
			}
			
		}
		
		return allBlocks;
	}
	
	private void genVarDeclBlockInform() {
		for(VarDeclareBlock block: this.varDeclBlocks) {
			//若是声明时初始化，eblockid就是leftVarid
			String regexVarUse = "\\s(Parm)?Var\\s(0x[0-9a-z]+)\\s'([_a-zA-Z]+[_a-zA-Z0-9]*)'\\s'([_a-zA-Z]+[_a-zA-Z0-9\\s\\(\\)\\*\\[\\]]*)'";	
			Pattern p = Pattern.compile(regexVarUse);
			Matcher m;
			
			block.setLeftVar(block.getId());
			if(block.getIsCinit() == true) {
				for(int temp = block.getAstStartLine(); temp < block.getAstEndLine(); temp++) {
					String line = astList.get(temp);
					m = p.matcher(line);
					if(m.find()) {
						block.getRightVar().add(m.group(2));
					}
				}
			}
			if(block.getEndLine() < block.getStartLine()) {
				
			}
		}
	}
	
	private void genEblockInform() {
		String regexVarUse = "\\s(Parm)?Var\\s(0x[0-9a-z]+)\\s'([_a-zA-Z]+[_a-zA-Z0-9]*)'\\s'([_a-zA-Z]+[_a-zA-Z0-9\\s\\(\\)\\*\\[\\]]*)'";	
		Pattern p = Pattern.compile(regexVarUse);
		Matcher m;
		//等式EqualBlock右边是否使用函数？
		String regexEuqalOpFunc = "<.*>.*\\sFunction\\s0x[a-z0-9]+\\s'([_a-zA-Z]+[_a-zA-Z0-9]*)'\\s'.*'";
		Pattern pEqualOpFunc = Pattern.compile(regexEuqalOpFunc);
		Matcher mEqualOpFunc;
		
		for(EqualOperatorBlock eblock: this.equalOpBlocks) {
			int begin = eblock.getAstStartLine();
			int end = eblock.getAstEndLine();
			int temp;
			
			//若是声明时初始化，eblockid就是leftVarid
			if(eblock.getIsCinit() == true) {
				eblock.setLeftVar(eblock.getId());
				for(temp = begin; temp < end; temp++) {
					String line = astList.get(temp);
					m = p.matcher(line);
					if(m.find()) {
						eblock.getRightVar().add(m.group(2));
						if(eblock.getRightFirstVarAstLine() == 0) eblock.setRightFirstVarAstLine(temp+1);
					}
					mEqualOpFunc = pEqualOpFunc.matcher(line);
					if(mEqualOpFunc.find()) {
						eblock.setHasFunction(true);
						if(eblock.getRightFirstFunAstLine() == 0 ) eblock.setRightFirstFunAstLine(temp+1);
						eblock.getFunctionName().add(mEqualOpFunc.group(1));
					}
				}
				
			}else {
				//find leftVar
				for(temp = begin-1 ; temp < end; temp++) {
					String line = astList.get(temp);
					m = p.matcher(line);
					if(m.find()) {
						eblock.setLeftVar(m.group(2));
						break;
					}
				}
				for(temp = temp+1; temp < end; temp++) {
					String line = astList.get(temp);
					m = p.matcher(line);
					if(m.find()) {
						eblock.getRightVar().add(m.group(2));
						if(eblock.getRightFirstVarAstLine() == 0) eblock.setRightFirstVarAstLine(temp+1);
					}
					mEqualOpFunc = pEqualOpFunc.matcher(line);
					if(mEqualOpFunc.find()) {
						eblock.setHasFunction(true);
						if(eblock.getRightFirstFunAstLine() == 0 ) eblock.setRightFirstFunAstLine(temp+1);
						eblock.getFunctionName().add(mEqualOpFunc.group(1));
					}
				}
			}
		}
	}
	
	private boolean isAstStart(String line, String filepath) {
		//String regexStart = ".*[a-zA-Z]+\\s0x[a-z0-9]+\\s<" + filepath + ".*,\\s.*>.*"; 
		String regexStart  = ".*[a-zA-Z]+\\s0x[a-z0-9]+\\s<" + filepath + ".*:([0-9]+):[0-9]+(,\\s.*)?>.*";
		if(line.matches(regexStart)) {
			this.isStart = true;
		}
		return this.isStart;
	}
	
	private void genAstList(String line) {
		//终端显示和读入的line有些许差别，存在终端显示一行、实际读入两行（多行）的情况
		//String lineStartChar = "^[^\\s`\\|].*";	//astLastElement存在warning/error
		String lineStartChar = "^[^`\\|].*";
		//errors and warnings generated
		String deleteChars = "([0-9]+\\s[(warning(s)?)(error(s)?)]+\\s)(and\\s[0-9]+\\s[(warning(s)?)(error(s)?)]+\\s)*(generated.)";
		
		//line是lineStartChar开头，或者line没有字符时，上一行有error或者warning
		if((line.replaceFirst("^\\s*", "").matches(lineStartChar) || line.matches("\\s*"))&& astList.size() != 0) {
			int lastIndex = astList.size()-1;
			String lastEle = astList.get(lastIndex); 
			Pattern p = Pattern.compile(deleteChars);
			Matcher m = p.matcher(lastEle);
			int s_index = 0, e_index = 0;
			String deletePart = "", left = "", right = "";
			int len = 0;
			if(m.find()) {
				s_index = m.start();
				e_index = m.end();
				deletePart = m.group();
				left = lastEle.substring(0, s_index);
				right = lastEle.substring(e_index);
				len = left.length() - left.lastIndexOf("0x");
				//位于中间的情况，把id "0x..."截开
				if(len < 9 && len < left.length()) {
					Pattern p0x = Pattern.compile("^([a-z0-9]+)");
					Matcher m0x;
					m0x = p0x.matcher(right);
					if(m0x.find()) {
						len += m0x.group(1).length();
					}
					if(len < 9) {
						m0x = p0x.matcher(line);
						if(m0x.find())
							len += m0x.group(1).length();
					}
					if(len < 9) {
						left += deletePart.substring(0, 9-len);
					}
				}else {
				//    |-IfStmt 0x7604ff8 <line:25:2, line:372 warnings generated.
				//:2> has_else
				//假设warning只有一位数？
					if(left.endsWith(":")) {
						String mid = lastEle.substring(s_index, e_index); //372 warnings generated.
						mid = mid.substring(0, mid.indexOf(" "));	//372
						left += mid.substring(0, mid.length()-1);
					}
				}
				astList.set(lastIndex, left + right + line);
			}
		}else {
			astList.add(line);
		}
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

