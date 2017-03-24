package ClauText;

public class Token
{
	public String str;
	public boolean isComment;
	public Token() { this.isComment = false; }
	public Token(String str) 
	{
		this(str, false);
	}
	public Token(String str, boolean isComment) 
	{
		this.str = str;
		this.isComment = isComment;
	}
}
