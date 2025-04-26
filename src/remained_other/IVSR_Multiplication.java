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
//import java.text.DecimalFormat;
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
//public class IVSR_Multiplication {
//	static List<String> initialList = new ArrayList<String>();
//	static int mutationCount = 0;
//	static final String[] allTypes = {"char", "short", "int", "long", "float", "double"};
//	static final String[] leftTypes = {"double", "float", "long", "int"};
//	
//	public static void main(String args[]) throws InterruptedException {
//		ivSRTransformation("00007.c");
//	}
//	//basicIV不能是临时变量
//	//targets derived IV strength reduction
//	public static void ivSRTransformation(String filename) throws InterruptedException{
//		try {
//			File file = new File("absolute " + filename);
//			initialList = ListOperation.genInitialList(file.getAbsolutePath());
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
//			String conditions = "";
//			
//			AstInform_Gen astgen = new AstInform_Gen();
//			astgen.getAstInform(file.getAbsolutePath());
////			LoopInform_Gen gen = new LoopInform_Gen(astgen);
////			List<LoopStatement> loopList = gen.outmostLoopList;
//			
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
//				    execTimes = LoopExecTimes.getTimes(filename, initialList, startLine, endLine);
//				    if(execTimes == 0) {
//				    	System.out.println("this loop infinite or not execute!");
//				    	continue;
//				    }
//				    if(loop.getForList().size() != 0) {
//				    	for(ForStatement f: loop.getForList()) {
//				    		varList = nestedIV(filename, ListOperation.getListPart(initialList, f.getStartLine(), f.getEndLine()), f, varList);
//				    	}
//				    }
//				    for(String s: loopBlock) {
//				    	transLoopBlock.add(s);
//				    }
//				    VariableDefine.renameReplace(transLoopBlock, loop.getUseVarList());
//				    //v是哪一层的变量
//				    for(AstVariable v: varList) {
//				    	String name = v.getName();
//				    	int count = startLine - 1;
//				    	int num = 0;
//				    	String op = "";
//				    	int lineNum = 0;
//				    	boolean isBasic = false;
//				    	String regex1 = "("+name+"\\s*=\\s*"+name+"\\s*(\\+|-)\\s*[0-9]+)|(\\s*"+name+"\\s*(\\+|-)=\\s*[0-9]+)";
//				    	String regex2 = "("+name+"\\s*(\\+\\+|--))|((\\+\\+|--)\\s*"+name+")";
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
//				    			if(group.contains("+")) {
//				    				op = "+";
//				    			}
//				    			else if(group.contains("-")) {
//				    				op = "-";
//				    			}
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
//				    			String group = matcher2.group();
//				    			if(group.contains("+")) {
//				    				op = "+";
//				    			}
//				    			else if(group.contains("-")) {
//				    				op = "-";
//				    			}
//				    			break;
//				    		}
//				    	}
//				    	if(isBasic) {
//				    		int[] varValue = LoopExecTimes.getBasicIVValue(filename, initialList, startLine, endLine, name);
//				    		BasicIV bi = new BasicIV(name, num, op, lineNum,  lineNum-startLine+1, "", varValue[0], varValue[1]);
//				    		loopBasicIVList.add(bi);
//				    	}
//				    	
//				    }
//				    
//				    for(BasicIV bi: loopBasicIVList) {
////				    	System.out.println("loop Basic IV:");
////				    	System.out.println("name: "+bi.getName());
////				    	System.out.println("num: "+bi.getNum());
////				    	System.out.println("op: "+bi.getOp());
////				    	System.out.println("lineNum: "+bi.getLineNum());
////				    	System.out.println("lineNumInLoop: "+bi.getLineNumInLoop());
//				    	
//				    	List<List<String>> listAll = genTransDerivedIV(transLoopBlock, bi, execTimes);
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
//	    
//	    if(selfFor.getForList().size() != 0) {
//	    	for(ForStatement f: selfFor.getForList()) {
//	    		varList = nestedIV(filename, ListOperation.getListPart(initialList, f.getStartLine(), f.getEndLine()), f, varList);
//	    	}
//	    }
//	    for(String s: nestedBlock) {
//	    	transLoopBlock.add(s);
//	    }
//	    VariableDefine.renameReplace(transLoopBlock, selfFor.getUseVarList());
//	    //v是哪一层的变量
//	    for(int j=0; j<varList.size(); j++) {
//	    	AstVariable v = varList.get(j);
//	    	String name = v.getName();
//	    	int count = selfFor.getStartLine() - 1;
//	    	int num = 0;
//	    	String op = "";
//	    	int lineNum = 0;
//	    	boolean isBasic = false;
//	    	String regex1 = "("+name+"\\s*=\\s*"+name+"\\s*(\\+|-)\\s*[0-9]+)|(\\s*"+name+"\\s*(\\+|-)=\\s*[0-9]+)";
//	    	String regex2 = "("+name+"\\s*(\\+\\+|--))|((\\+\\+|--)\\s*"+name+")";
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
//	    			if(group.contains("+")) {
//	    				op = "+";
//	    			}
//	    			else if(group.contains("-")) {
//	    				op = "-";
//	    			}
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
//	    			String group = matcher2.group();
//	    			if(group.contains("+")) {
//	    				op = "+";
//	    			}
//	    			else if(group.contains("-")) {
//	    				op = "-";
//	    			}
//	    			break;
//	    		}
//	    	}
//	    	if(isBasic) {
//	    		lastVar.remove(varList.get(j));
//	    		int[] varValue = LoopExecTimes.getBasicIVValue(filename, initialList, selfFor.getStartLine(), selfFor.getEndLine(), name);
//	    		BasicIV bi = new BasicIV(name, num, op, lineNum,  lineNum-selfFor.getStartLine()+1, "", varValue[0], varValue[1]);
//	    		loopBasicIVList.add(bi);
//	    	}
//	    	
//	    }
//	    
//	    if(execTimes == 0) {
//	    	System.out.println("this loop infinite or not execute!");
//	    	return lastVar;
//	    }
//	    
//	    for(BasicIV bi: loopBasicIVList) {
////	    	System.out.println("loop Basic IV:");
////	    	System.out.println("name: "+bi.getName());
////	    	System.out.println("num: "+bi.getNum());
////	    	System.out.println("op: "+bi.getOp());
////	    	System.out.println("lineNum: "+bi.getLineNum());
////	    	System.out.println("lineNumInLoop: "+bi.getLineNumInLoop());
//	    	
//	    	List<List<String>> listAll = genTransDerivedIV(transLoopBlock, bi, execTimes);
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
//	//for(){一行 }单独一行
//	//c*i 如果c type是char或者short 则c*abs(i)max在规定范围呢 要么就去掉char或者short类型
//
//	public static List<List<String>> genTransDerivedIV(List<String> loopBlock, BasicIV bi, int arraySize) {
//		List<String> derivedDef = new ArrayList<String>();
//		List<String> derivedUse = new ArrayList<String>();
//		List<String> transformedList = new ArrayList<String>();
//		List<String> conditionList = new ArrayList<String>();
//		List<List<String>> listAll = new ArrayList<List<String>>();
//		Random random = new Random();
//		String basicIVName = bi.getName();
//		String op1 = random.nextBoolean()? "+" : "-";
//		
//		String[] types = getRandomTypes();//array c d e
//		String[] names = getRandomVarNames();
//		String newTempName1 = "s_" + getRandomVarName();
//		List<String> typeList1 = new ArrayList<String>();
//		typeList1.add(types[1]);
//		typeList1.add(types[2]);
//		typeList1.add("int");
//
//		//derivedDef
//		derivedDef.add(types[0] + " " + names[0] + "[" + arraySize + "];");
//		derivedDef.add(types[1] + " " + names[1] + " = " + getRandomNumRestriction(types[1], bi.getVarStartValue(), bi.getVarEndValue()) +  ";");
//		derivedDef.add(types[2] + " " + names[2] + " = " + getRandomNum(types[2]) + ";");
//		derivedDef.add("int iiii = 0;");	
//		
//		//derivedUse
//		derivedUse.add(names[0] + "[iiii]" + " = " 
//				+ names[1] + " * " + basicIVName + " " + op1 + " " + names[2] 
//				+ ";");
//		derivedUse.add("iiii++;");
//		
//		//transformedList
//		transformedList.add(types[0] + " " + names[0] + "1[" + arraySize + "];");
//		transformedList.add("int iiii1 = 0;");
//		//transformed declare ends
//		if(bi.getLineNumInLoop() != 1) {
//			transformedList.add(getTopType(typeList1) + " " + newTempName1 + " = " 
//					+ names[1] + " * (" + basicIVName + "1 " + bi.getOp() + " " + bi.getNum() + ") " + op1 + " " + names[2] + ";");
//		}
//		else {
//			transformedList.add(getTopType(typeList1) + " " + newTempName1 + " = " 
//				+ names[1] + " * " + basicIVName + "1 " + op1 + " " + names[2] + ";");
//		}
//		
//		
//		for(int i=0;i<loopBlock.size();i++) {
//			if(i == bi.getLineNumInLoop()){
//				transformedList.add(names[0] + "1[iiii1] = " + newTempName1 + ";");
//				transformedList.add(newTempName1 + " = " + newTempName1 + " " + bi.getOp() + " " + names[1] 
//						+ " * " + bi.getNum() + ";");
//				transformedList.add("iiii1++;");
//			}
//	
//			transformedList.add(loopBlock.get(i));
//		}
////		ListOperation.printList(transformedList);
//		
//		//conditionList
//		if(types[0].equals("double") || types[0].equals("float")) {
//			conditionList.add("for(int i=0; i<" + arraySize + "; i++){");
//			conditionList.add("if(fabs(" + names[0]  + "[i] - " + names[0] + "1[i]) >= 0.000001)   goto error;");
//			conditionList.add("}");
//		}
//		else {
//			conditionList.add("for(int i=0; i<" + arraySize + "; i++){");
//			conditionList.add("if(" + names[0]  + "[i] != " + names[0] + "1[i])   goto error;");
//			conditionList.add("}");
//		}
//		
//		listAll.add(derivedDef);
//		listAll.add(derivedUse);
//		listAll.add(transformedList);
//		listAll.add(conditionList);
//		return listAll;
//	}
//	
//	
//	//leftType, rightType*2
//	//左	>=右 如果左边是double或者float 则右边只能是整型
//	//变化过程中，必须保证等式左右两边的toptype一一对应
//	//c*i_max不能做截断???在finding过程中需要跑程序等到i的max
//	public static String[] getRandomTypes() {
//		String[] randomTypes = new String[3];
//		Random random = new Random();
//		
//		int leftIndex = random.nextInt(leftTypes.length);
//		randomTypes[0] = leftTypes[leftIndex];
//		if(leftIndex == 0 || leftIndex == 1) {
//			leftIndex = 2;
//		}
//		randomTypes[1] = allTypes[random.nextInt(allTypes.length - leftIndex)];
//		randomTypes[2] = allTypes[random.nextInt(allTypes.length - leftIndex)];
//
//		for(int i=0; i<3; i++) {
//			if(!randomTypes[i].equals("float") && !randomTypes[i].equals("double")) {
//				randomTypes[i] = random.nextBoolean()? randomTypes[i]: "unsigned " + randomTypes[i];
//			}
//		}
//		
//		return randomTypes;
//	}
//	
//	public static String getTopType(List<String> some) {
//		String top = "";
//		int maxIndex = 0;
//		Map<String, Integer> priorityType = new HashMap<String, Integer>();
//		priorityType.put("char", 1);
//		priorityType.put("unsigned char", 2);
//		priorityType.put("short", 3);
//		priorityType.put("unsigned short", 4);
//		priorityType.put("int", 5);
//		priorityType.put("unsigned int", 6);
//		priorityType.put("long", 7);
//		priorityType.put("unsigned long", 8);
//		priorityType.put("float", 9);
//		priorityType.put("double", 10);
//		
//		for(String s: some) {
//			if(priorityType.get(s) > maxIndex) {
//				top = s;
//				maxIndex = priorityType.get(s);
//			}
//		}
//		return top;
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
//		String[] randomNames = new String[3];
//		randomNames[0] = "left_" + getRandomVarName();
//		randomNames[1] = "c_" + getRandomVarName();
//		randomNames[2] = "d_" + getRandomVarName();
//		return randomNames;
//	}
//	
//	//type: c's type
//	public static String getRandomNumRestriction(String type, int min, int max) {
//		List<String> types = new ArrayList<String>();
//		types.add(type);
//		types.add("int");
//		String topType = getTopType(types);
//		String number = "";
//		boolean flag = true;
//		while(flag) {
//			number = getRandomNum(type);
//			if(topType.equals("int")) {
//				if(-(int)Math.pow(2, 31) <= Integer.parseInt(number) * min && Integer.parseInt(number) * min < (int)Math.pow(2, 31) && 
//						-(int)Math.pow(2, 31) <= Integer.parseInt(number) * max && Integer.parseInt(number) * max < (int)Math.pow(2, 31)){
//					flag = false;
//				}
//				System.out.println(type + ":" + flag);
//			}
//			else if(topType.equals("unsigned int")) {
//				if(0 <= Integer.parseInt(number) * min && Integer.parseInt(number) * min < (int)Math.pow(2, 32) && 
//						0 <= Integer.parseInt(number) * max && Integer.parseInt(number) * max < (int)Math.pow(2, 32)){
//					flag = false;
//				}
//				System.out.println(type + ":" + flag);
//			}
//			else if(topType.equals("long")) {
//				if(-(long)Math.pow(2, 63) <= Long.parseLong(number) * min && Long.parseLong(number) * min < (long)Math.pow(2, 63) &&
//						-(long)Math.pow(2, 63) <= Long.parseLong(number) * max && Long.parseLong(number) * max < (long)Math.pow(2, 63)){
//					flag = false;
//				}
//				System.out.println(type + ":" + flag);
//			}
//			else if(topType.equals("unsigned long")) {
//				if(0 <= Long.parseLong(number) * min && Long.parseLong(number) * min < (long)Math.pow(2, 64) && 
//						0 <= Long.parseLong(number) * max && Long.parseLong(number) * max < (long)Math.pow(2, 64)){
//					flag = false;
//				}
//				System.out.println(type + ":" + flag);
//			}
//		}
//		System.out.println("end end!");
//		return number;
//	}
//	
//	public static String getRandomNum(String type) {
//		Random random = new Random();
//		DecimalFormat df = new DecimalFormat("#0.00");
//		if(type.equals("char")) {
//			return String.valueOf(random.nextInt(-(int)Math.pow(2, 7),(int)Math.pow(2, 7)));
//		}
//		else if(type.equals("unsigned char")) {
//			return String.valueOf(random.nextInt((int)Math.pow(2, 8)));
//		}
//		else if(type.equals("short")) {
//			return String.valueOf(random.nextInt(-(int)Math.pow(2, 15),(int)Math.pow(2, 15)));
//		}
//		else if(type.equals("unsigned short")) {
//			return String.valueOf(random.nextInt((int)Math.pow(2, 16)));
//		}
//		else if(type.equals("int")) {
//			return String.valueOf(random.nextInt(-(int)Math.pow(2, 31),(int)Math.pow(2, 31)));
//		}
//		else if(type.equals("unsigned int")) {
//			return String.valueOf(random.nextInt((int)Math.pow(2, 32)));
//		}
//		else if(type.equals("long")) {
//			return String.valueOf(random.nextLong(-(int)Math.pow(2, 63),(int)Math.pow(2, 63)));
//		}
//		else if(type.equals("unsigned long")) {
//			return String.valueOf(random.nextLong((int)Math.pow(2, 64)));
//		}
//		else if(type.equals("float")) {
//			return String.valueOf(df.format(random.nextFloat(-(int)Math.pow(2, 31),(int)Math.pow(2, 31))));
//		}
//		else if(type.equals("double")) {
//			return String.valueOf(df.format(random.nextDouble(-(int)Math.pow(2, 63),(int)Math.pow(2, 63))));
//		}
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
