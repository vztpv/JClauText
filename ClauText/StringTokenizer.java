package ClauText;

import java.util.ArrayList;

public class StringTokenizer
{
	public ArrayList<String> whitespaces;
	private ArrayList<String> _m_str;
	private int count;
	private boolean exist;
	private int option;
	
	private void Init(String str, ArrayList<String> separator) throws Exception // assumtion : separators are sorted by length?, long . short
	{
		if (separator.isEmpty() || str.isEmpty()) { return; } // if str.empty() == false then _m_str.push_back(str); // ?

		int left = 0;
		int right = 0;
		int state = 0;
		this.exist = false;

		for (int i = 0; i < str.length(); ++i) {
			right = i;
			int _select = -1;
			boolean pass = false;
			
			if (1 == option && 0 == state && '\"' == str.charAt(i)) {
				if (i == 0) {
					state = 1;
					continue;
				}
				else if (i > 0 && '\\' == str.charAt(i-1)) {
					throw new Exception("ERROR for Init on StringTokenizer"); // error!
				}
				else if (i > 0) {
					state = 1;
					continue;
				}
			}
			else if (1 == option && 1 == state && '\"' == str.charAt(i)) {
				if ('\\' == str.charAt(i-1)) {
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
				for (int k = 0; k < separator.get(j).length() && i + k < str.length(); ++k) {
					if (str.charAt(i + k) == separator.get(j).charAt(k)) {
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
					_m_str.add(str.substring(left, right));
				}
				i = i + separator.get(_select).length() - 1;
				left = i + 1;
				right = left;
			}
			else if (!pass && i == str.length() - 1) {
				if (right - left + 1 > 0) {
					_m_str.add(str.substring(left, right+1));
				}
			}
		}
	}

	public StringTokenizer(String str, String separator, int option) throws Exception
	{
		this.count = 0;
		this.exist = false;
		this.option = option;
		ArrayList<String> vec = new ArrayList<String>(); vec.add(separator);
		Init(str, vec);
	}
	public StringTokenizer(String str, String separator) throws Exception
	{
		this(str, separator, 0);
	}
	public StringTokenizer( String str,  ArrayList<String> separator, int option) throws Exception
	{
		this.count = 0;
		this.exist = false;
		this.option = option;
		
		Init(str, separator);
	}
	public StringTokenizer(String str, ArrayList<String> separator) throws Exception
	{
		this(str, separator, 0);
	}
	public StringTokenizer(String str, int option) throws Exception
	{
		this.count = 0;
		this.exist = false;
		this.option = option;
		
		whitespaces = new ArrayList<String>();
		whitespaces.add(" ");
		whitespaces.add("\t");
		whitespaces.add("\r");
		whitespaces.add("\n");
		
		Init(str, whitespaces);
	}
	public StringTokenizer(String str) throws Exception
	{
		this(str, 0);
	}
	public int countTokens()
	{
		return _m_str.size();
	}
	public String nextToken() throws Exception
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
