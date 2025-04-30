package expression_operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
	public static Map<String, Integer> operator = new HashMap<String, Integer>() {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
            put("+", 1);
            put("-", 1);
            put("*", 2);
            put("/", 2);
        }
    };
	
    
    public static ArrayList<String> getSubExpressions( String expression ) {
    	ExpressionBinaryTree tree = new ExpressionBinaryTree(expression);
    	ArrayList<String> subExpressionsList = new ArrayList<String>();
    	getSubExpressions(tree.getRoot(), subExpressionsList);
    	return subExpressionsList;
    }
    
    public static ArrayList<String> getOperands(String expression) {
    	ExpressionBinaryTree tree = new ExpressionBinaryTree(expression);
    	ArrayList<String> operandsList = new ArrayList<String>();
    	getOperands(tree.getRoot(), operandsList);
    	return operandsList;
    }

    public static String addSpaceToSM(String stmt) {
		if(stmt.contains("=") == false) return stmt;
		String regex = "([0-9a-zA-Z_)][\\+\\-\\*/]?[\\+\\-\\*/=])";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(stmt);
		while(m.find()) {
			String oldstr = m.group(1);
			String replacement = oldstr.charAt(0) + " " + oldstr.substring(1) + " ";
			stmt = stmt.replace(oldstr, replacement);
		}
		return stmt;
	}
    
    //更新exp：将exp中的字符串oldStr替换为newStr
    public static String ExpReplace(String exp, String oldStr, String newStr) {
		String newExp = "";
		int index = 0;
		String regex = "(\\W|^)" + oldStr + "(\\W|$)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(exp);
		while(m.find()) {
			int start = m.start();
			if(start > 0 && exp.charAt(start) == '.') continue ;	//结构体Struct S A; A.a中的点；
			else if(exp.substring(0,start+1).matches(".*->\\s*")) continue;	//结构体指针Sttuct S *A; A->a中的a
			else newExp += exp.substring(index, start) + m.group().replace(oldStr, newStr);
			index = m.end();
		}
		newExp += exp.substring(index, exp.length());
		return newExp;
	}
    
    private static void getSubExpressions(BinaryTreeNode root, ArrayList<String> subExpressionList){
    	if( root == null ) return ;
    	subExpressionList.add( getExpressionByTree(root, true) );
    	getSubExpressions(root.leftChild, subExpressionList);
    	getSubExpressions(root.rightChild, subExpressionList);
    }
    
    private static String getExpressionByTree(BinaryTreeNode root, boolean isExpressionRoot) {
    	if( root == null ) return "";
    	if( root.leftChild == null ) return root.getNodeVal();
    	String leftBrace = "";
    	String rightBrace = "";
    	//若不是表达式的根节点，则判断是否要加括号
    	if( !isExpressionRoot ) {
    		String childOp = root.getNodeVal();
    		String parentOp = root.parent.getNodeVal();
    		if( operator.get(childOp) < operator.get(parentOp) ) {
    			leftBrace = "(";
    			rightBrace = ")";
    		}else if(operator.get(childOp) == operator.get(parentOp)) {
    			if(root.getWhichChild().equals("rightChild")) {
    				leftBrace = "(";
    				rightBrace = ")";
    			}
    			/*if(parentOp.equals("-") && root.getWhichChild().equals("rightChild") ) {
    				//System.out.print("childOp: " + childOp + "; ");
    	    		//System.out.print("parentOp: " + parentOp + "; ");
    	    		//System.out.println("whichChild: " + whichChild);
    				leftBrace = "(";
        			rightBrace = ")";
    			}else if(parentOp.equals("/") && root.getWhichChild().equals("rightChild")) {
    				leftBrace = "(";
        			rightBrace = ")";
    			}
    			*/
    		}
    	}
    	return leftBrace + getExpressionByTree(root.leftChild, false) + root.getNodeVal() + getExpressionByTree(root.rightChild, false) + rightBrace;
    }
    
    private static void getOperands(BinaryTreeNode node, ArrayList<String> operandsList){
    	if( node == null ) return ;
    	if( node.leftChild == null ) operandsList.add(node.getNodeVal());
    	getOperands(node.leftChild, operandsList);
    	getOperands(node.rightChild, operandsList);
    }
}