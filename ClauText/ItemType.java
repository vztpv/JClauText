package ClauText;

public class ItemType extends Type {
	private String data;
	private boolean inited;
	
	public ItemType()
	{
		super("");
		this.data = "";
		this.inited = false;
	}
	public ItemType(String name, String value)
	{
		super(name);
		this.data = value;
		this.inited = true;
	}
	public void Remove()
	{
		data = "";
	}
	public boolean Push(String val) throws Exception { /// do not change..!!
		if (inited) { throw new Exception("ItemType already inited"); }
		this.data = val;
		this.inited = true;

		return true;
	}
	public String Get() throws Exception {
		if (!inited) { throw new Exception("ItemType, not inited");  }
		return this.data;
	}
	public void Set(String val) throws Exception {
		if (!inited) { throw new Exception("ItemType, not inited"); } // removal?
		this.data = val;
	}
	public int size() {
		return inited? 1 : 0;
	}
	public boolean empty() { return !inited; }
}
