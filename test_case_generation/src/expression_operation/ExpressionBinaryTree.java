package expression_operation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class ExpressionBinaryTree {
	
	private BinaryTreeNode root = null;
	private String expression = null;
	
    //表达式优先级
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
    
    public BinaryTreeNode getRoot() {
    	return this.root;
    }
    
    public String getExpression() {
    	return this.expression;
    }
    
  //判断字符是否是数字
    public static boolean isNumber(Character character) {
        return Character.isDigit(character);
    }

    public static boolean isLetter(Character character) {
    	return Character.isLetter(character);
    }
    
    public static boolean isBrace(Character character) {
    	String str = Character.toString(character);
    	return str.equals("(") || str.equals(")");
    }
    
    public static boolean isOperator(char character) {
    	if(character == '+') return true;
    	if(character == '-') return true;
    	if(character == '*') return true;
    	if(character == '/') return true;
    	return false;
    }

    //根据中缀表达式创建二叉树
    public ExpressionBinaryTree(String expression) {
        //去除空格
        expression = expression.replaceAll(" ", "");
        this.expression = expression;
        char[] chars = expression.toCharArray();
        int len = chars.length;
        //操作符存储栈
        LinkedList<BinaryTreeNode> stackOperator = new LinkedList<>();
        //操作数存储栈
        LinkedList<BinaryTreeNode> stackOperand = new LinkedList<>();
        BinaryTreeNode node = null;
        BinaryTreeNode leftChild = null;
        BinaryTreeNode rightChild = null;
        for (int i = 0; i < len; i++) {
            int j;
        	char c = chars[i];
            //"("直接入栈
            if (c == '(') {
            	node = new BinaryTreeNode("(", "leftBrace");
                stackOperator.push(node);
            } else if (c == ')') {
                //出栈所有操作符到"（"节点为止
                while (!stackOperator.isEmpty()) {
                    //当前操作符作为栈顶操作符的右子树
                    if (!Objects.equals(stackOperator.peek().getNodeVal(), "(")) {
                        node = stackOperator.pop();	//弹出顶部操作符
                        rightChild = stackOperand.pop();
                        leftChild = stackOperand.pop();
                        node.setRightChild(rightChild);
                        node.setLeftChild(leftChild);
                        rightChild.setWhichChild("rightChild");
                        leftChild.setWhichChild("leftChild");
                        rightChild.setParent(node);
                        leftChild.setParent(node);
                        stackOperand.push(node);
                    } else {
                        //"("直接出栈
                        stackOperator.pop();
                        break;
                    }
                }
            } else {//不是括号的情况下
                //若是常数
                if (isNumber(c)) {
                    StringBuilder num = new StringBuilder(c + "");
                    j = i + 1;
                    boolean isDigital = false;
                    while (j < len) {
                    	if( isDigital == false ) {
                    		if( isNumber(chars[j]) ) {
                    			num.append(chars[j++]);
                    		}else if( chars[j] == '.') {
                    			isDigital = true;
                    			num.append(chars[j++]);
                    		}else {
                    			break;
                    		}
                    	}else {
                    		if( isNumber(chars[j]) ){
                    			num.append(chars[j++]);
                    		}else {
                    			break;
                    		}
                    	}
                	}
                    i = j - 1;
                    node = new BinaryTreeNode(num.toString(), "const");
                    stackOperand.push(node);
                }
                //若是变量
                if(isLetter(c) || Objects.equals(c, '_') ) {
                	StringBuilder var = new StringBuilder(c + "");
                    j = i + 1;
                    while (j < len && (isOperator(chars[j]) == false) && (isBrace(chars[j]) == false) ) {
                        var.append(chars[j++]);
                    }
                    i = j - 1;
                    //System.out.println(var);
                    node = new BinaryTreeNode(var.toString(), "var");
                    stackOperand.push(node);
                }
                //若是操作符
                if(isOperator(c)){
                    //构建操作符节点p
                    node = new BinaryTreeNode(Character.toString(c), "operator");
                    BinaryTreeNode topOperator;
                    if( !stackOperator.isEmpty() ) {
                    	topOperator = stackOperator.peek();	//栈顶操作符
                        if (!Objects.equals(topOperator.getNodeVal(), "(")) {
                            //如果栈顶操作符优先级低于当前操作符优先级 直接入栈
                            if (operator.get(topOperator.getNodeVal()) < operator.get(String.valueOf(c))) {
                                /*rightChild = node;
                            	topOperator.setRightChild(rightChild);
                            	rightChild.setWhichChild("rightChild");
                                rightChild.setParent(topOperator);
                                */
                            	stackOperator.push(node);
                            } else {
                                //同级或栈顶操作符优先级高于当前操作符，则栈顶操作符是当前操作符的左孩子节点
                                if (!Objects.equals(topOperator.getNodeVal(), "(")) {
                                    topOperator = stackOperator.pop();
                                    rightChild = stackOperand.pop();
                                    leftChild = stackOperand.pop();
                                    topOperator.setRightChild(rightChild);
                                    topOperator.setLeftChild(leftChild);
                                    rightChild.setWhichChild("rightChild");
                                    leftChild.setWhichChild("leftChild");
                                    rightChild.setParent(topOperator);
                                    leftChild.setParent(topOperator);
                                    node.setLeftChild(topOperator);
                                    topOperator.setWhichChild("leftChild");
                                    topOperator.setParent(node);
                                    stackOperand.push(topOperator);
                                    stackOperator.push(node);
                                }
                            }
                        } else {
                            //如果栈顶元素是"（"，操作符节点设置左孩子
                        	leftChild = stackOperand.peek();
                        	node.setLeftChild(leftChild);
                        	leftChild.setWhichChild("leftChild");
                        	leftChild.setParent(node);
                            stackOperator.push(node);
                        }
                    } else {
                    	//操作符栈为空
                    	leftChild = stackOperand.peek();
                    	node.setLeftChild(leftChild);
                    	leftChild.setWhichChild("leftChild");
                    	leftChild.setParent(node);
                        stackOperator.push(node);
                    }
                }
            }
        }

        //出栈所有操作符
        while (!stackOperator.isEmpty()) {
            BinaryTreeNode treeNode = stackOperator.pop();
            rightChild = stackOperand.pop();
            leftChild = stackOperand.pop();
            treeNode.setRightChild(rightChild);
            treeNode.setLeftChild(leftChild);
            rightChild.setWhichChild("rightChild");
            leftChild.setWhichChild("leftChild");
            rightChild.setParent(treeNode);
            leftChild.setParent(treeNode);
            stackOperand.push(treeNode);
        }
  
        this.root = stackOperand.pop();

    }

    //表达式二叉树求值
    /*static int getExpression(BinaryTreeNode root) {
        switch (root.val) {
            case "+": return getExpression(root.left) + getExpression(root.right);
            case "-": return getExpression(root.left) - getExpression(root.right);
            case "*": return getExpression(root.left) * getExpression(root.right);
            case "/": return getExpression(root.left) / getExpression(root.right);
            default: return Integer.parseInt(root.val);
        }
    }*/
    
    
	/*
	 * public static String getExpression(BinaryTreeNode root) { if( root == null )
	 * return ""; if( root.leftChild == null ) return root.getNodeVal(); String
	 * leftBrace = ""; String rightBrace = ""; if() return ""; }
	 */
    
     
}

