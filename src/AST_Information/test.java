package AST_Information;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_Information.Inform_Gen.*;
import AST_Information.model.AstVariable;
import AST_Information.model.ForStatement;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AstInform_Gen astgen = new AstInform_Gen();
		astgen.getAstInform("/home/jing/桌面/testInitial/pr33870-1.c");
		ForInform_Gen forgen = new ForInform_Gen(astgen);
		for(ForStatement stmt: forgen.outmostForList) {
			System.out.println(stmt.getStartLine() + " " + stmt.getEndLine());
			System.out.println("\tuseVar:");
			for(AstVariable var: stmt.getUseVarList()) {
				System.out.println(var.getName() + " " + var.getType() + " " + var.getDeclareLine());
			}
			System.out.println("\toutsideVar:");
			for(AstVariable var: stmt.getOutsideList()) {
				System.out.println(var.getName() + " " + var.getType() + " " + var.getDeclareLine());
			}
			System.out.println("\tinsideVar:");
			for(AstVariable var: stmt.getInsideList()) {
				System.out.println(var.getName() + " " + var.getType() + " " + var.getDeclareLine());
			}
			System.out.println();
		}
	}

}
