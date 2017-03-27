package ClauText;

public class ItemType extends Type {
	private WrapString data;
	private boolean inited;
	
	public ItemType()
	{
		super(new WrapString(""));
		this.data = new WrapString("");
		this.inited = false;
	}
	public ItemType(WrapString name, WrapString value)
	{
		super(name);
		this.data = value;
		this.inited = true;
	}
	public void Remove()
	{
		data = new WrapString("");
	}
	public boolean Push(WrapString val) throws Exception { /// do not change..!!
		if (inited) { throw new Exception("ItemType already inited"); }
		this.data = val;
		this.inited = true;

		return true;
	}
	public WrapString Get() throws Exception {
		if (!inited) { throw new Exception("ItemType, not inited");  }
		return this.data;
	}
	public void Set(WrapString val) throws Exception {
		if (!inited) { throw new Exception("ItemType, not inited"); } // removal?
		this.data = val;
	}
	public int size() {
		return inited? 1 : 0;
	}
	public boolean empty() { return !inited; }
}
