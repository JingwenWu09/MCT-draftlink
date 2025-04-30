package Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_Information.model.IfStatement;

public class IfBlock {
	/*ifBlock的格式为
	 * 1. if(){
	 * ...
	 * }else {
	 * ...
	 * }
	 * 
	 * 2. if(){
	 * ...
	 * }else if(){
	 * ...
	 * }
	 * */
	private String condition = "";
	private List<String> body_if = null;
	private List<String> body_else = null;
	private List<String> whole_else = null;
	
	public IfBlock(){
		
	}
	
	
	public void genIfBlock_HasElse(IfStatement stmt, List<String> initialList) {
		int elseline = stmt.getElseline();
//		System.out.println("s: " + stmt.getStartLine() + "; e: " + stmt.getEndLine() + "; " + elseline);
		body_if = new ArrayList<String>();
		whole_else = new ArrayList<String>();
		
		int startline = stmt.getStartLine();
		String ifhead = initialList.get(startline);
		this.condition = getIfCondition(ifhead);
		
		//find whether "if" Has Brace
		int ifBodySline = startline + 1;
		int ifBodyEline = elseline - 1;
		boolean ifHasBrace = false;
		if(ifhead.matches("^.*\\{\\s*$")) {
			ifHasBrace = true;
		}else {
			for( int i = startline+1; i < elseline; i++) {
				String line = initialList.get(i);
				if(line.matches("^\\s*\\{\\s*$")) {
					ifBodySline = i+1;
					ifHasBrace = true;
					break ;
				}else if(line.matches("^\\s*$")) {
					continue ;
				}else break;
			}
		}
		if(ifHasBrace == true) {
			for( int i = elseline; i > ifBodySline; i--) {
				if(initialList.get(i).matches("^\\s*}.*$")){
					ifBodyEline = i-1;
					break ;
				}
			}
		}
		for( int i = ifBodySline; i <= ifBodyEline; i++) {
			body_if.add(initialList.get(i));
		}
		whole_else.add( initialList.get(elseline).replaceFirst("}\\s*", "") );
		for( int i = elseline + 1; i <= stmt.getEndLine(); i++) {
			whole_else.add(initialList.get(i));
		}
	}
	
	public List<String> getInnerMostBodyElse(List<String> whole_else){
		body_else = new ArrayList<String>();
		int start = 1;
		int end = whole_else.size()-1;
		boolean ifHasBrace = false;
		if(whole_else.get(end).matches("^\\s*}\\s*$")) {
			ifHasBrace = true;
			end = end - 1;
		}
		if(ifHasBrace == true) {
			for( int i = 0; i < end; i++) {
				if(whole_else.get(i).matches("^.*\\{\\s*")) {
					start = i + 1;
					break ;
				}
			}
		}
		
		for( int i = start; i <= end; i++) {
			body_else.add(whole_else.get(i));
		}
		return body_else;
		
	}
	
	
	
	public void genIfBlock_HasNotElse(IfStatement stmt, List<String> initialList) {
		body_if = new ArrayList<String>();
		
		int startline = stmt.getStartLine();
		int endline = stmt.getEndLine();
		this.condition = getIfCondition(initialList.get(startline));
		
		//find whether "if" Has Brace
		int ifBodySline = startline + 1;
		int ifBodyEline = endline - 1;
		boolean ifHasBrace = false;
		if(initialList.get(endline).matches("^\\s*}\\s*$")) ifHasBrace = true;

		if(ifHasBrace == true) {
			for( int i = startline; i < endline; i++) {
				String line = initialList.get(i);
				if(line.matches("^.*\\{\\s*$")) {
					ifBodySline = i+1;
					break ;
				}
			}
		}else ifBodyEline = endline;
		
		for( int i = ifBodySline; i <= ifBodyEline; i++) {
			body_if.add(initialList.get(i));
		}
	}
	
	public static String getIfCondition(String ifhead) {
		String regexIf = "\\bif\\b\\s*\\((.*)\\)\\s*";
		Pattern pIf = Pattern.compile(regexIf);
		Matcher mIf = pIf.matcher(ifhead);
		String if_condition = "";
		if(mIf.find()) {
			if_condition = mIf.group(1);
		}
		return if_condition;
	}
	
	//有child，whole_else
	public void genIfBlock_HasChild(IfStatement stmt, List<String> initialList){
		body_if = new ArrayList<String>();
		whole_else = new ArrayList<String>();
		
		int startline = stmt.getStartLine();
		int endline = stmt.getEndLine();
		String ifhead = initialList.get(startline);
		
		condition = getIfCondition(initialList.get(startline));
		
		//2. get if_body and else_body
		if(stmt.getHasElse() == false) {
		//no else
			if(initialList.get(endline).matches("^\\s*}\\s*$")) {
			//no else, hasBrace
				boolean findLeftBrace = false;
				int i = startline;
				while(findLeftBrace == false) {
					if(initialList.get(i).matches("^.*\\{\\s*$")) {
						for( i=i+1; i < endline; i++) {
							body_if.add(initialList.get(i));
						}
						break;
					}else i++;
				}
			}else {
			//no else, no brace
				for( int i = startline+1; i <= endline; i++) {
					body_if.add(initialList.get(i));
				}
			}
		}else {
		//has else
			//find else startline
			int ifBraceline = -1;
			if(ifhead.matches("^.*\\{\\s*$")) {
				ifBraceline = startline;
				ifhead = ifhead.substring(0, ifhead.lastIndexOf('{'));
			}
			//find else & add whole_else
			int i = startline + 1;
			for( ; i < endline; i++) {
				String line = initialList.get(i);
				if(line.matches("^.*\\belse\\b.*$")) {
				//find else
					if(line.matches("^\\s*}.*$"));{
						line = line.replaceFirst("}\\s*", "");
					}
					whole_else.add(line);
					for( int j = i+1; j <= endline; j++) {
						whole_else.add(initialList.get(j));
					}
					break ;
				}else if(ifBraceline == -1) {
				//is not else startline, and find whether If has brace
					if(line.startsWith("\\s*}.*")) {
						ifBraceline = i;
					}
				}
			}
			//i is else startline;
			if(ifBraceline != -1) {
			//if has brace
				for(int j = ifBraceline+1; j <= i; j++ ) {
					String line = initialList.get(j);
					if(line.matches("^\\s*}.*$")) break;
					body_if.add(line);
				}
			}else {
			// if without brace
				for(int j = startline+1; j < i; j++) {
					body_if.add(initialList.get(j));
				}
			}
		}
		
	}
	
	//无child, body_else
	public void genIfBlock_HasNotChild(IfStatement stmt, List<String> initialList){
		body_if = new ArrayList<String>();
		body_else = new ArrayList<String>();
		
		int startline = stmt.getStartLine();
		int endline = stmt.getEndLine();
		String ifhead = initialList.get(startline);
		
		//1. get if condtion
		String regexIf = "\\bif\\s*\\((.*)\\)\\s*";
		Pattern pIf = Pattern.compile(regexIf);
		Matcher mIf = pIf.matcher(ifhead);
		if(mIf.find()) {
			condition = mIf.group(1);
		}
		
		//2. get if_body and else_body
		if(stmt.getHasElse() == false) {
		//no else
			if(initialList.get(endline).matches("^\\s*}\\s*$")) {
			//no else, hasBrace
				boolean findLeftBrace = false;
				int i = startline;
				while(findLeftBrace == false) {
					if(initialList.get(i).matches("^.*\\{\\s*$")) {
						for( i=i+1; i < endline; i++) {
							body_if.add(initialList.get(i));
						}
						break;
					}else i++;
				}
			}else {
			//no else, no brace
				for( int i = startline+1; i <= endline; i++) {
					body_if.add(initialList.get(i));
				}
			}
		}else {
		//has else
			//find else startline
			int ifBraceline = -1;
			if(ifhead.matches("^.*\\{\\s*$")) {
				ifBraceline = startline;
				ifhead = ifhead.substring(0, ifhead.lastIndexOf('{'));
			}
			//find else & add body_else
			int i = startline + 1;
			for( ; i < endline; i++) {
				String line = initialList.get(i);
				if(line.matches("^.*\\belse\\b.*$")) {
				//find else
					if(initialList.get(endline).matches("^\\s*}\\s*$")) {
					//else hasBrace
						boolean findLeftBrace = false;
						int j = i;
						while(findLeftBrace == false) {
							if(initialList.get(j).matches("^.*\\{\\s*$")) {
								for( j=j+1; j < endline; j++) {
									body_else.add(initialList.get(j));
								}
								break;	//此时 i 是 else startline
							}else j++;
						}
					}else {
					//else without brace
						for( int j = i+1; j <= endline; j++) {
							body_else.add(initialList.get(j));
						}
					}
				}else if(ifBraceline == -1) {
				//is not else startline, and find whether If has brace
					if(line.startsWith("\\s*}.*")) {
						ifBraceline = i;
					}
				}
			}
			//i is else startline;
			if(ifBraceline != -1) {
			//if has brace
				for(int j = ifBraceline+1; j <= i; j++ ) {
					String line = initialList.get(j);
					if(line.matches("^\\s*}.*$")) break;
					body_if.add(line);
				}
			}else {
			// if without brace
				for(int j = startline+1; j < i; j++) {
					body_if.add(initialList.get(j));
				}
			}
		}
		
	}
	
	public String getCondtion() {
		return this.condition;
	}
	public List<String> getBodyIf(){
		return this.body_if;
	}
	public List<String> getBodyElse(){
		return this.body_else;
	}
	public List<String> getWholeElse(){
		return this.whole_else;
	}
	
}
