package DataFlow;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import AST_Information.model.AstVariable;

public class VariableOperation {
	
	public static int[] getAddedLineBound(List<String> functionBody, AstVariable var,  int bodyStartline, int bodyReturnline) {
		int[] bound = new int[2];
		bound[0] = getLeastAddedLine(functionBody, var, bodyStartline, bodyReturnline);
		bound[1] = getLastAddedLine(functionBody, var, bodyStartline, bodyReturnline);
		return bound;
	}
	
	public static int getLeastAddedLine(List<String> functionBody, AstVariable var, int bodyStartline, int bodyReturnline){
		//bodyStartline指的是{出现的下一行
		if(var.getIsIntialized()) {
			int declareline = var.getDeclareLine();
			if(declareline >= bodyStartline && declareline < bodyReturnline) {
				return declareline;
			}
		}
		
		for(int line: var.useLine) {
			if(line >= bodyStartline && line < bodyReturnline) {
				int index = line;
				if((index+1) < bodyReturnline && functionBody.get(line - bodyStartline + 1).matches("^\\s*\\{")) {
					line = line+1;
				}
				return line;
			}
		}
		
		return bodyStartline-1;	//bodyStartline-1是 { 这一行， 是在addedline之后添加addStmt
	}
	
	public static int getLastAddedLine(List<String> functionBody, AstVariable var, int bodyStartline, int bodyReturnline) {
		
		for(int i = var.useLine.size()-1; i >= 0; i--) {
			int line = var.useLine.get(i);
			if(line >= bodyStartline && line < bodyReturnline) {
				return line;
			}
		}
		
		if(var.getIsIntialized()) {
			int declareline = var.getDeclareLine();
			if(declareline >= bodyStartline && declareline < bodyReturnline) {
				return declareline;
			}
		}
		
		return bodyStartline;
	}
	

	public static String getMaxType(String[] varTypes, String type1, String type2) {
		String type = "";
		for(int i = varTypes.length-1; i > 0; i--) {
			if(varTypes[i].equals(type1)) return type1;
			if(varTypes[i].equals(type2)) return type2;
		}
		return type;
	}
	
	public static List<String[]> genRandomVars(int n, String[] varTypes){
		
		List<String[]> newVars = new ArrayList<String[]>();
		String[] names = genRandomName(n);
		String[] types = genRandomType(n, varTypes);
		for(int i = 0; i < n; i++) {
			String newVar[] = new String[3];
			newVar[0] = types[i];
			newVar[1] = names[i];
			newVar[2] = genRandomValue(types[i]);
			while(Math.abs(Double.parseDouble(newVar[2])) < 0.000001) newVar[2] = genRandomValue(types[i]);
			newVars.add(newVar);
		}
		
		return newVars;
	}
	
	public static String[] genRandomName(int n) {
		
		String names[] = new String[n];
		String str="_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		//String str = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    Random random = new Random();
	    //int randomLength  = random.nextInt(1,6);
	    for(int cnt = 0; cnt < n; cnt++) {
	    	String name = "";
	    	int randomLength = (int) (Math.random()*2 + 3);
		    for(int i = 0;i < randomLength;i++){
		    	int number = random.nextInt(53);	//
		    	name += str.charAt(number);
		    }
		    names[cnt] = name;
	    }
	    
	    return names;
	}
	
	public static String genRandomName() {
		String str="_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		//String str = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    Random random = new Random();
	    //int randomLength  = random.nextInt(1,6);
	    String name = "";
    	int randomLength = (int) (Math.random()*2 + 3);
	    for(int i = 0;i < randomLength;i++){
	    	int number = random.nextInt(53);	//
	    	name += str.charAt(number);
	    }
	    
	    return name;
	}
	
	public static String[] genRandomType(int n, String[] varTypes) {
		String[] types = new String[n];
		Random random = new Random();
		for(int cnt = 0; cnt < n; cnt++) {
		    types[cnt] = varTypes[random.nextInt(varTypes.length)];
	    }
		return types;
	}
	
	public static String genRandomType(String[] varTypes) {
		
		Random random = new Random();
		return varTypes[random.nextInt(varTypes.length)];
	}
	
	public static String genRandomValue(String type) {
		String value = null;
		DecimalFormat df = new DecimalFormat("#0.00");	//浮点数，保留两位小数
		Random random = new Random();
		
		if(type.contains("unsigned char")) {
			return String.valueOf(random.nextInt((int)Math.pow(2, 8)));
		}
		else if(type.contains("char")) {
			//return String.valueOf(random.nextInt(-(int)Math.pow(2, 7),(int)Math.pow(2, 7)));
			return String.valueOf(random.nextInt((int)Math.pow(2, 7)));
		}
		else if(type.contains("unsigned short")) {
			return String.valueOf(random.nextInt((int)Math.pow(2, 16)));
		}
		else if(type.contains("short")) {
			//return String.valueOf(random.nextInt(-(int)Math.pow(2, 15),(int)Math.pow(2, 15)));
			return String.valueOf(random.nextInt((int)Math.pow(2, 15)));
		}
		else if(type.contains("unsigned long")) {
			//return String.valueOf(random.nextLong((int)Math.pow(2, 64)));
			return String.valueOf(random.nextLong());
		}
		else if(type.contains("long")) {
			//return String.valueOf(random.nextLong(-(int)Math.pow(2, 63),(int)Math.pow(2, 63)));
			return String.valueOf(random.nextLong());
		}
		else if(type.equals("unsigned int")) {
			return String.valueOf(random.nextInt((int)Math.pow(2, 32)));
		}
		else if(type.contains("int")) {
			//return String.valueOf(random.nextInt(-(int)Math.pow(2, 31),(int)Math.pow(2, 31)));
			return String.valueOf(random.nextInt((int)Math.pow(2, 31)));
		}
		
		//random无法生成float和double的上下限？？？
		else if(type.equals("float")) {
			return String.valueOf(df.format(random.nextFloat()*1000000));
		}
		else if(type.equals("double")) {
			return String.valueOf(df.format(random.nextDouble()*1000000000));
		}
		return value;
	}
}
