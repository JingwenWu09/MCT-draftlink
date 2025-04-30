package AST_Information;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.Inform_Gen.LoopInform_Gen;
import AST_Information.model.AstVariable;
import AST_Information.model.LoopStatement;

public class testLoop {

	public static void main(String[] args) {
//		String filepath = "/home/jing/桌面/pr101983-main.c";
//		String regexStartLine2 = ".*[a-zA-Z]+\\s0x[a-z0-9]+\\s<" + filepath + ".*?:([0-9]+):[0-9]+(,\\s.*)?>.*"; 
//		Pattern p = Pattern.compile(regexStartLine2);
//		String line = "|   `-DoStmt 0x7642110 </home/jing/桌面/pr101983-main.cihiline:34:15>";
//		Matcher m = p.matcher(line);
//		if(m.find()) {
//			System.out.println(m.group(1));
//		}
		
	
//		// TODO Auto-generated method stub
//		String filepath = "/home/jing/桌面/pr101983-main.c";
//		File file = new File(filepath);
//		AstInform_Gen astgen = new AstInform_Gen();
//		astgen.getAstInform(filepath);
//		LoopInform_Gen loopgen = new LoopInform_Gen(astgen);
//		List<LoopStatement> outmostLoopList = loopgen.outmostLoopList;
//		for(LoopStatement stmt: outmostLoopList) {
//			printfLoop(stmt);
//		}
	}
	
	public static void printfLoop(LoopStatement stmt) {
		if(stmt == null) return ;
		System.out.println(stmt.getStmtType() + " " + stmt.getStartLine() + " " + stmt.getEndLine());
		for(AstVariable var: stmt.getUseVarList()) {
			System.out.println(var.getName() + " " + var.getType() + " " +var.getKind());
		}
		System.out.println();
		
		for(LoopStatement child: stmt.getLoopList()) {
			printfLoop(child);
		}
	}

}
