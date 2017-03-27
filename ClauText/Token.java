package ClauText;

public class Token
{
	public WrapString str;
	public boolean isComment;
	public Token() { this.isComment = false; }
	public Token(WrapString str) 
	{
		this(str, false);
	}
	public Token(WrapString str, boolean isComment) 
	{
		this.str = str;
		this.isComment = isComment;
	}
}
