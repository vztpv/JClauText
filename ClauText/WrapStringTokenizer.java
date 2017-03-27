package ClauText;

import java.util.ArrayList;

public class WrapStringTokenizer
{
	public ArrayList<WrapString> whitespaces;
	private ArrayList<WrapString> _m_str;
	private int count;
	private boolean exist;
	private int option;
	
	private void Init(WrapString str, ArrayList<WrapString> separator) throws Exception // assumtion : separators are sorted by length?, long . short
	{
		if (separator.isEmpty() || str.data.isEmpty()) { return; } // if str.data.empty() == false then _m_str.data.push_back(str); // ?

		int left = 0;
		int right = 0;
		int state = 0;
		this.exist = false;

		for (int i = 0; i < str.data.length(); ++i) {
			right = i;
			int _select = -1;
			boolean pass = false;
			
			if (1 == option && 0 == state && '\"' == str.data.charAt(i)) {
				if (i == 0) {
					state = 1;
					continue;
				}
				else if (i > 0 && '\\' == str.data.charAt(i-1)) {
					throw new Exception("ERROR for Init on WrapStringTokenizer"); // error!
				}
				else if (i > 0) {
					state = 1;
					continue;
				}
			}
			else if (1 == option && 1 == state && '\"' == str.data.charAt(i)) {
				if ('\\' == str.data.charAt(i-1)) {
					continue;
				}
				else {
					state = 0;
				}
			}
			else if (1 == option && 1 == state) {
				continue;
			}


			for (int j = 0; j < separator.size(); ++j) {
				for (int k = 0; k < separator.get(j).data.length() && i + k < str.data.length(); ++k) {
					if (str.data.charAt(i + k) == separator.get(j).data.charAt(k)) {
						pass = true;
					}
					else {
						pass = false;
						break;
					}
				}
				if (pass) { _select = j; break; }
			}

			if (pass) {
				this.exist = true;

				if (right-1 - left + 1 > 0) {
					_m_str.add(new WrapString(str.data.substring(left, right)));
				}
				i = i + separator.get(_select).data.length() - 1;
				left = i + 1;
				right = left;
			}
			else if (!pass && i == str.data.length() - 1) {
				if (right - left + 1 > 0) {
					_m_str.add(new WrapString(str.data.substring(left, right+1)));
				}
			}
		}
	}

	public WrapStringTokenizer(WrapString str, WrapString separator, int option) throws Exception
	{
		this.count = 0;
		this.exist = false;
		this.option = option;
		ArrayList<WrapString> vec = new ArrayList<WrapString>(); vec.add(separator);
		Init(str, vec);
	}
	public WrapStringTokenizer(WrapString str, WrapString separator) throws Exception
	{
		this(str, separator, 0);
	}
	public WrapStringTokenizer( WrapString str,  ArrayList<WrapString> separator, int option) throws Exception
	{
		this.count = 0;
		this.exist = false;
		this.option = option;
		
		Init(str, separator);
	}
	public WrapStringTokenizer(WrapString str, ArrayList<WrapString> separator) throws Exception
	{
		this(str, separator, 0);
	}
	public WrapStringTokenizer(WrapString str, int option) throws Exception
	{
		this.count = 0;
		this.exist = false;
		this.option = option;
		
		whitespaces = new ArrayList<WrapString>();
		whitespaces.add(new WrapString(" "));
		whitespaces.add(new WrapString("\t"));
		whitespaces.add(new WrapString("\r"));
		whitespaces.add(new WrapString("\n"));
		
		Init(str, whitespaces);
	}
	public WrapStringTokenizer(WrapString str) throws Exception
	{
		this(str, 0);
	}
	public int countTokens()
	{
		return _m_str.size();
	}
	public WrapString nextToken() throws Exception
	{
		if (hasMoreTokens())
			return _m_str.get(count++);
		else
			throw new Exception("error in nextToken!");
	}
	public boolean hasMoreTokens()
	{
		return count < _m_str.size();
	}

	public boolean isFindExist()
	{
		return exist;
	}

};
