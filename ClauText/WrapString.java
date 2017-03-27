package ClauText;

public class WrapString {
	public String data;
	
	public WrapString() { this(""); }
	public WrapString(String data) {
		this.data = data;
	}
	
	public WrapString(WrapString var1) {
		this(var1.data);
	}
	public void swap( WrapString other)
	{
		String temp = other.data;
		other.data = this.data;
		this.data = temp; 
	}
	public boolean equals(WrapString obj)
	{
		return this.data.equals(obj.data);
	}
}
