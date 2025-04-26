package remained_other;
//package iv_other;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import Inform_Gen.AstInform_Gen;
//import Inform_Gen.ForInform_Gen;
//import iv_operation.LoopExecTimes;
//import list_operation.ListOperation;
//import model.ForStatement;
//import model.AstVariable;
//import variable_operation.VariableDefine;
//
//public class IVSR_Exponent {
//	static List<String> initialList = new ArrayList<String>();
//	static int mutationCount = 0;
//	static final String[] allTypes = {"char", "short", "int"};
//	
//	public static void main(String args[]) throws InterruptedException {
//		ivSRTransformation("00007.c");
//	}
//	//basicIV不能是临时变量
//	//targets derived IV strength reduction
//	public static void ivSRTransformation(String filename) throws InterruptedException{
//		try {
//			File file  = new File("/Users/elowen/Desktop/ModelChecking/example/singleIVTestcase/"+filename);
//			InputStreamReader in = new InputStreamReader(new FileInputStream(file));
//			BufferedReader bf = new BufferedReader(in);
//			
//			
//			List<AstVariable> varList = new ArrayList<AstVariable>();//一个loop中inside和outside的varset
//			List<String> loopBlock = new ArrayList<String>();//一个loop的stringlist
//			List<String> transLoopBlock = new ArrayList<String>();
//			List<String> varDeclareList = new ArrayList<String>();
//			List<BasicIV> loopBasicIVList = new ArrayList<BasicIV>();//一个loop中basic IV的集合
//			
//			Map<Integer, Integer> edge = new HashMap<Integer, Integer>();
//			Map<Integer, ForStatement> loopStart = new HashMap<Integer, ForStatement>();
//			Map<Integer, List<AstVariable>> loopVarSet = new HashMap<Integer, List<AstVariable>>();
//			int lineCount = 0;
//			int startLine = 0;
//			int endLine = 0;
//			int execTimes = 0;
//			ForStatement loop = new ForStatement();
//			String singleLine = "";
//			String conditions = "";
//		
//			while((singleLine = bf.readLine()) != null) {
//				initialList.add(singleLine);
//			}
//			
//			AstInform_Gen astgen = new AstInform_Gen();
//			astgen.getAstInform("/Users/elowen/Desktop/ModelChecking/example/singleIVTestcase/" + filename);
//			ForInform_Gen gen = new ForInform_Gen(astgen);
//			List<ForStatement> loopList = gen.outmostForList;
//			for(ForStatement singleLoop: loopList) {
//				edge.put(singleLoop.getStartLine(), singleLoop.getEndLine());
//				loopStart.put(singleLoop.getStartLine(),singleLoop);
//				List<AstVariable> vl = new ArrayList<AstVariable>();
//				
//				for(AstVariable v: singleLoop.getOutsideList()) {
//					vl.add(v);
//				}
//			    loopVarSet.put(singleLoop.getStartLine(), vl);
//
//			}
//						
//			for(String initialLine: initialList) {
//				lineCount++;
//				if(edge.containsKey(lineCount)) {
//					startLine = lineCount;
//					endLine = edge.get(lineCount);
//					loop = loopStart.get(lineCount);
//					varList = loopVarSet.get(lineCount);
//				    loopBlock = ListOperation.getListPart(initialList, startLine, endLine);
//				    varDeclareList = VariableDefine.getVarDeclareList(loop.getUseVarList());
//				    conditions = VariableDefine.getCondition(loop.getUseVarList());
//				    //????exectimes 无穷
//				    execTimes = LoopExecTimes.getTimes(filename, initialList, startLine, endLine);
//				    if(loop.getForList().size() != 0) {
//				    	for(ForStatement f: loop.getForList()) {
//				    		varList = nestedIV(filename, ListOperation.getListPart(initialList, f.getStartLine(), f.getEndLine()), f, varList);
//				    	}
//				    }
//				    for(String s: loopBlock) {
//				    	transLoopBlock.add(s);
//				    }
//				    VariableDefine.renameReplace(transLoopBlock, loop.getUseVarList());
//				    
//				    for(AstVariable v: varList) {
//				    	String name = v.getName();
//				    	int count = startLine - 1;
//				    	int num = 0;
//				    	String op = "";
//				    	int lineNum = 0;
//				    	boolean isBasic = false;
//				    	String regex1 = "("+name+"\\s*=\\s*"+name+"\\s*\\+\\s*[0-9]+)|(\\s*"+name+"\\s*\\+=\\s*[0-9]+)";
//				    	String regex2 = "("+name+"\\s*\\+\\+)|(\\+\\+\\s*"+name+")";
//				    	Pattern p1 = Pattern.compile(regex1);
//				    	Pattern p2 = Pattern.compile(regex2);
//				    	for(String s: loopBlock) {
//				    		count++;
//				    		Matcher matcher1 = p1.matcher(s);
//				    		Matcher matcher2 = p2.matcher(s);
//				    		if(matcher1.find()) {
//				    			isBasic = true;
//				    			lineNum = count;
//				    			String group = matcher1.group();
//				    			op = "+";
//				    			String str = "";
//				    			for(int i=0; i<group.length(); i++) {
//				    				if(group.charAt(i) >= 48 && group.charAt(i) <= 57) {
//				    					str += group.charAt(i);
//				    				}
//				    			}
//				    			num = Integer.parseInt(str);
//				    			break;
//				    		}
//				    		else if(matcher2.find()) {
//				    			isBasic = true;
//				    			lineNum = count;
//				    			num = 1;
//				    			op = "+";
//				    			break;
//				    		}
//				    	}
//				    	if(isBasic) {
//				    		int varValue = LoopExecTimes.getVarValue(filename, initialList, endLine, name);
//				    		System.out.println("VarValue: "+varValue);
//				    		BasicIV bi = new BasicIV(name, num, op, lineNum,  lineNum-startLine+1, "", varValue);
//				    		loopBasicIVList.add(bi);
//				    	}
//				    	
//				    }
//				    
//				    for(BasicIV bi: loopBasicIVList) {
//				    	System.out.println("loop Basic IV:");
//				    	System.out.println("name: "+bi.getName());
//				    	System.out.println("num: "+bi.getNum());
//				    	System.out.println("op: "+bi.getOp());
//				    	System.out.println("lineNum: "+bi.getLineNum());
//				    	System.out.println("lineNumInLoop: "+bi.getLineNumInLoop());
//				    	
//				    	List<List<String>> listAll = genTransDerivedIV(transLoopBlock, bi, execTimes, bi.getVarEndValue());
//				    	addDerivedIV(filename, initialList, startLine, endLine, bi.getLineNum()+1, varDeclareList, conditions, listAll);
//				    }
//				    
//				    startLine = 0;
//				    endLine = 0;
//				    loop = new ForStatement();
//				    varDeclareList = new ArrayList<String>();
//				    conditions = "";
//				    varList = new ArrayList<AstVariable>();
//				    loopBlock = new ArrayList<String>();
//				    transLoopBlock = new ArrayList<String>();
//				    loopBasicIVList = new ArrayList<BasicIV>();
//				    
//				    continue;
//				}
//				
//			}
//			
//			bf.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	
//	public static List<AstVariable> nestedIV(String filename, List<String> nestedBlock, ForStatement selfFor, List<AstVariable> lastVar) throws IOException, InterruptedException {
//	
//		List<AstVariable> varList = new ArrayList<AstVariable>();//一个nestedloop中outside的varset+外层loop的outside
//		List<String> transLoopBlock = new ArrayList<String>();
//		List<String> varDeclareList = new ArrayList<String>();
//		List<BasicIV> loopBasicIVList = new ArrayList<BasicIV>();//一个nestedloop中basic IV的集合
//		
//		int execTimes = 0;
//		String conditions = "";
//		
//		for(AstVariable v: lastVar) {
//			varList.add(v);
//		}
//		for(AstVariable v: selfFor.getOutsideList()) {
//			varList.add(v);
//		}
//		
//	    varDeclareList = VariableDefine.getVarDeclareList(selfFor.getUseVarList());
//	    conditions = VariableDefine.getCondition(selfFor.getUseVarList());
//	    execTimes = LoopExecTimes.getTimes(filename, initialList, selfFor.getStartLine(), selfFor.getEndLine());
//	    if(selfFor.getForList().size() != 0) {
//	    	for(ForStatement f: selfFor.getForList()) {
//	    		varList = nestedIV(filename, ListOperation.getListPart(initialList, f.getStartLine(), f.getEndLine()), f, varList);
//	    	}
//	    }
//	    for(String s: nestedBlock) {
//	    	transLoopBlock.add(s);
//	    }
//	    VariableDefine.renameReplace(transLoopBlock, selfFor.getUseVarList());
//	    
//	    for(int j=0; j<varList.size(); j++) {
//	    	AstVariable v = varList.get(j);
//	    	String name = v.getName();
//	    	int count = selfFor.getStartLine() - 1;
//	    	int num = 0;
//	    	String op = "";
//	    	int lineNum = 0;
//	    	boolean isBasic = false;
//	    	String regex1 = "("+name+"\\s*=\\s*"+name+"\\s*\\+\\s*[0-9]+)|(\\s*"+name+"\\s*\\+=\\s*[0-9]+)";
//	    	String regex2 = "("+name+"\\s*\\+\\+)|(\\+\\+\\s*"+name+")";
//	    	Pattern p1 = Pattern.compile(regex1);
//	    	Pattern p2 = Pattern.compile(regex2);
//	    	for(String s: nestedBlock) {
//	    		count++;
//	    		Matcher matcher1 = p1.matcher(s);
//	    		Matcher matcher2 = p2.matcher(s);
//	    		if(matcher1.find()) {
//	    			isBasic = true;
//	    			lineNum = count;
//	    			String group = matcher1.group();
//	    			op = "+";
//	    			String str = "";
//	    			for(int i=0; i<group.length(); i++) {
//	    				if(group.charAt(i) >= 48 && group.charAt(i) <= 57) {
//	    					str += group.charAt(i);
//	    				}
//	    			}
//	    			num = Integer.parseInt(str);
//	    			break;
//	    		}
//	    		else if(matcher2.find()) {
//	    			isBasic = true;
//	    			lineNum = count;
//	    			num = 1;
//	    			op = "+";
//	    			break;
//	    		}
//	    	}
//	    	if(isBasic) {
//	    		int varValue = LoopExecTimes.getVarValue(filename, initialList, selfFor.getEndLine(), name);
//	    		System.out.println("VarValue: "+varValue);
//	    		lastVar.remove(varList.get(j));
//	    		BasicIV bi = new BasicIV(name, num, op, lineNum,  lineNum-selfFor.getStartLine()+1, "", varValue);
//	    		loopBasicIVList.add(bi);
//	    	}
//	    	
//	    }
//	    
//	    for(BasicIV bi: loopBasicIVList) {
//	    	System.out.println("loop Basic IV:");
//	    	System.out.println("name: "+bi.getName());
//	    	System.out.println("num: "+bi.getNum());
//	    	System.out.println("op: "+bi.getOp());
//	    	System.out.println("lineNum: "+bi.getLineNum());
//	    	System.out.println("lineNumInLoop: "+bi.getLineNumInLoop());
//	    	
//	    	List<List<String>> listAll = genTransDerivedIV(transLoopBlock, bi, execTimes, bi.getVarEndValue());
//	    	addDerivedIV(filename, initialList, selfFor.getStartLine(), selfFor.getEndLine(), bi.getLineNum()+1, varDeclareList, conditions, listAll);
//	    }
//
//		return lastVar;
//	}
//	
//	//bi不是loop中的临时变量!!!
//	//如果是临时变量 需要把声明放在loop外-> deal with individual
//	//bi在循环中的初始值需要放在loop body外
//	//该loop指的是bi模式使用在哪个loop中
//	
//	//只有x=x+c这种形式 不可饮出现x=x-c，即不可以用除法
//	//e不可以是double或者float 不然最后小数位置不同
//	//抛除unsigned
//	//i 的值不能是负数 int i=0; i<num; i+=number --2
//
//	public static List<List<String>> genTransDerivedIV(List<String> loopBlock, BasicIV bi, int arraySize, int varValue) {
//		List<String> derivedDef = new ArrayList<String>();
//		List<String> derivedUse = new ArrayList<String>();
//		List<String> transformedList = new ArrayList<String>();
//		List<String> conditionList = new ArrayList<String>();
//		List<List<String>> listAll = new ArrayList<List<String>>();
//		Random random = new Random();
//		String basicIVName = bi.getName();
//		
//		String e_type = allTypes[random.nextInt(allTypes.length)];
//		String[] names = getRandomVarNames();
//		String newTempName1 = "s_" + getRandomVarName();
//		//String eNum = getRandomNum(e_type);
//		String eNum = String.valueOf(random.nextInt(145));
//		while(Math.pow(Double.valueOf(eNum), varValue)>Double.MAX_VALUE) {
//			System.out.println("check restriction>>>>>>>");
//			eNum = getRandomNum(e_type);
//		}
//
//		//derivedDef
//		derivedDef.add("double " + names[0] + "[" + arraySize + "];");
//		derivedDef.add(e_type + " " + names[1] + " = " + eNum +  ";");
//		derivedDef.add("int iiii = 0;");	
//		
//		//derivedUse
//		derivedUse.add(names[0] + "[iiii]" + " = " 
//				+  "pow(" + names[1] + ", " + basicIVName + ");");
//		derivedUse.add("iiii++;");
//		
//		//transformedList
//		transformedList.add("double " + names[0] + "1[" + arraySize + "];");
//		transformedList.add("int iiii1 = 0;");
//		//transformed declare ends
//		if(bi.getLineNumInLoop() != 1) {
//			transformedList.add("double " + newTempName1 + " = pow(" + names[1]
//					 + ", " + "(" + basicIVName + "1 " + bi.getOp() + " " + bi.getNum() + "));");
//		}
//		else {
//			transformedList.add("double " + newTempName1 + " = pow(" + names[1]
//					 + ", " + basicIVName + "1);" );
//		}
//		
//		for(int i=0;i<loopBlock.size();i++) {
//			if(i == bi.getLineNumInLoop()){
//				transformedList.add(names[0] + "1[iiii1] = " + newTempName1 + ";");
//				if(bi.getOp().equals("+")) {
//					for(int j=0; j<bi.getNum(); j++) {
//						transformedList.add(newTempName1 + " = " + newTempName1 + " * " + names[1] + ";");
//					}
//				}
//				else if(bi.getOp().equals("-")) {
//					for(int j=0; j<bi.getNum(); j++) {
//						transformedList.add(newTempName1 + " = " + newTempName1 + " / " + names[1] + ";");
//					}
//				}
//				transformedList.add("iiii1++;");
//			}
//	
//			transformedList.add(loopBlock.get(i));
//		}
////		ListOperation.printList(transformedList);
//		
//		//conditionList
//		conditionList.add("for(int ij=0; ij<" + arraySize + "; ij++){");
//		conditionList.add("if(fabs(" + names[0]  + "[ij] - " + names[0] + "1[ij]) >= 0.000001)   goto error;");
//		conditionList.add("}");
//		
//		listAll.add(derivedDef);
//		listAll.add(derivedUse);
//		listAll.add(transformedList);
//		listAll.add(conditionList);
//		return listAll;
//	}
//	
//	public static String getRandomVarName() {
//		String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		//String str = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//	    Random random = new Random();
//	    //int randomLength  = random.nextInt(1,6);
//	    int randomLength = (int) (Math.random()*2 + 3);
//	    StringBuffer sb = new StringBuffer();
//	    for(int i = 0;i < randomLength;i++){
//	    	int number = random.nextInt(52);	//
//	    	//int number = random.nextInt(63);
//	    	sb.append(str.charAt(number));
//	    }
//	    return sb.toString();
//	}
//	
//	public static String[] getRandomVarNames() {
//		String[] randomNames = new String[2];
//		randomNames[0] = "left_" + getRandomVarName();
//		randomNames[1] = "e_" + getRandomVarName();
//		return randomNames;
//	}
//	
//	public static String getRandomNum(String type) {
//		Random random = new Random();
//		if(type.equals("char")) {
//			return String.valueOf(random.nextInt(-(int)Math.pow(2, 7),(int)Math.pow(2, 7)));
//		}
//		else if(type.equals("short")) {
//			return String.valueOf(random.nextInt(-(int)Math.pow(2, 15),(int)Math.pow(2, 15)));
//		}
//		else if(type.equals("int")) {
//			return String.valueOf(random.nextInt(-(int)Math.pow(2, 31),(int)Math.pow(2, 31)));
//		}
////		else if(type.equals("long")) {
////			return String.valueOf(random.nextLong(-(int)Math.pow(2, 63),(int)Math.pow(2, 63)));
////		}
//		return null;
//	}
//	
//	
//	public static void addDerivedIV(String filename, List<String> initialList, int startLine, int endLine, int writeLine, List<String> varDeclareList, String conditions, List<List<String>> listAll) {
//		try {
//			String dir = "/Users/elowen/Desktop/ModelChecking/example/singleIVTestcase/addedDerived/";
//			File newFile = new File(dir+filename.substring(0, filename.lastIndexOf("."))+"_"+mutationCount+filename.substring(filename.lastIndexOf(".")));
//			mutationCount++;
//
//			if(newFile.exists()) {
//				newFile.delete();
//			}
//			newFile.createNewFile();
//			FileWriter fw = new FileWriter(newFile, true);
//			PrintWriter pw = new PrintWriter(fw);
//			
//			int lineCount = 0;
//			List<String> derivedDef = listAll.get(0);
//			List<String> derivedUse = listAll.get(1);
//			List<String> transformedList = listAll.get(2);
//			List<String> conditionList = listAll.get(3);
//			
//			for(String line: initialList) {
//				lineCount++;
//				if(lineCount == 1) {
//					pw.println("#include<math.h>");
//					pw.println("#include<stdlib.h>");
//				}
//				if(lineCount == initialList.size()) {
//					pw.println("error: exit(0);");
//				}
//				if(lineCount == startLine) {
//					varDeclareList.forEach( s -> {
//						pw.println(s.trim());
//					});
//					derivedDef.forEach( s -> {
//						pw.println(s.trim());
//					});
//				}
//				if(lineCount == writeLine) {
//					derivedUse.forEach( s -> {
//						pw.println(s.trim());
//					});
//				}
//				if(lineCount == endLine) {
//					pw.println(line.trim());
//					transformedList.forEach( s -> {
//						pw.println(s.trim());
//					});
//					//error: return or exit(0);????
//					if(!conditions.equals("")) {
//						String con = "if("+conditions+")  " + "goto error;"; 
//						pw.println(con);
//					}
//					conditionList.forEach( s -> {
//						pw.println(s.trim());
//					});
//					continue;
//				}
//				pw.println(line.trim());
//				
//			}
//			
//			pw.flush();
//			fw.flush();
//			pw.close();
//			fw.close();
//		}catch (Exception e) {
//			e.printStackTrace();
//		} 
//	}
//	
//}
