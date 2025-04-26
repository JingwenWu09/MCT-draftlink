package remained_other;

public class DerivedIV {
	private String name;
	private int c;
	private BasicIV j;
	private int d;
	//name = c * j + d;
	
	public DerivedIV(String name, int c, BasicIV j, int d) {
		super();
		this.name = name;
		this.c = c;
		this.j = j;
		this.d = d;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	public BasicIV getJ() {
		return j;
	}
	public void setJ(BasicIV j) {
		this.j = j;
	}
	public int getD() {
		return d;
	}
	public void setD(int d) {
		this.d = d;
	}
	
	
}
