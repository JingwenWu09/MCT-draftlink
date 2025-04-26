package remained_other;

import java.util.List;
import java.util.Map;

import unrolling_operation.Number;

public class UnrollingForBlock {
	int endLine;
	String header;
	private List<String> inner;
	private Map<String, Integer> strNum;
	private List<Number> numberList;
	private Map<String, String> numOp;
	
	public UnrollingForBlock(int endLine, String header, List<String> inner, Map<String, Integer> strNum, List<Number> numberList,Map<String, String> numOp) {
		this.endLine = endLine;
		this.header = header;
		this.inner = inner;
		this.strNum = strNum;
		this.numberList = numberList;
		this.numOp = numOp;
	}
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public int getEndLine() {
		return endLine;
	}
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}
	public List<String> getInner() {
		return inner;
	}
	public void setInner(List<String> inner) {
		this.inner = inner;
	}
	public Map<String, Integer> getStrNum() {
		return strNum;
	}
	public void setStrNum(Map<String, Integer> strNum) {
		this.strNum = strNum;
	}
	public List<Number> getNumberList() {
		return numberList;
	}

	public void setNumberList(List<Number> numberList) {
		this.numberList = numberList;
	}
	public Map<String, String> getNumOp() {
		return numOp;
	}
	public void setNumOp(Map<String, String> numOp) {
		this.numOp = numOp;
	}
}
