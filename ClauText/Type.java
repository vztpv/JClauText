package ClauText;

public class Type {
	private WrapString name;
	
	public Type() { }
	public Type(WrapString name) { this.name = name; }

	public WrapString GetName() { return this.name; }
	public void SetName(WrapString val) { this.name = val; }
}
