package ClauText;

import java.util.LinkedList;

class NoneReserver extends Reserver
{
	private int count;
	public NoneReserver() { count = 0; }
	public boolean reserve(LinkedList<Token> strVec) 
	{
		count = 1;
		return false;
	}
	public boolean end() { return 1 == count; }
}
