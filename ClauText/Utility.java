package ClauText;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import ClauText.UserType;

public class Utility {
	// check this method! - maybe has bug!
	private static boolean ChkComment(LinkedList<Token> strVec, UserType ut, Reserver reserver, int offset) throws Exception
	{
		if (strVec.size() < offset) {
			reserver.reserve(strVec);
			while (strVec.size() < offset) // 
			{
				reserver.reserve(strVec);
				if (
					strVec.size() < offset &&
					reserver.end()
					) {
					return false;
				}
			}
		}
		
		ListIterator<Token> x = strVec.listIterator();
		boolean tokenHasNext = x.hasNext();
		Token token = tokenHasNext? x.next() : null;
		
		int count = 0;

		while(tokenHasNext) {
			if (token.isComment) {
				ut.PushComment(token.str);
				// x = strVec.erase(x); in c++

				x.remove();
				tokenHasNext = x.hasNext();
				token = tokenHasNext? x.next() : null;
			}
			else if (count == offset - 1) {
				return true;
			}
			else { 
				count++;
				tokenHasNext = x.hasNext();
				token = tokenHasNext? x.next() : null;
			}

			if (token == null) {
				reserver.reserve(strVec);
				x = strVec.listIterator(count);
				tokenHasNext = x.hasNext();
				token = tokenHasNext? x.next() : null;
				while (strVec.size() < offset) // + count?
				{
					reserver.reserve(strVec);
					x = strVec.listIterator(count);
					tokenHasNext = x.hasNext();
					token = tokenHasNext? x.next() : null;
					if (
						strVec.size() < offset &&
						reserver.end()
						) {
						return false;
					}
				}
			}
		}
		return false;
	}

	static WrapString Top(LinkedList<Token> strVec, UserType ut, Reserver reserver) throws Exception
	{
		if (strVec.isEmpty() || strVec.getFirst().isComment) {
			if (false == ChkComment(strVec, ut, reserver, 1)) {
				return new WrapString("");
			}
		}
		if (strVec.isEmpty()) { return new WrapString(""); }
		return strVec.getFirst().str;
	}
	
	static boolean Pop(LinkedList<Token> strVec, WrapString str, UserType ut, Reserver reserver) throws Exception
	{
		if (strVec.isEmpty() || strVec.getFirst().isComment) {
			if (false == ChkComment(strVec, ut, reserver, 1)) {
				return false;
			}
		}

		if (strVec.isEmpty()) {
			return false;
		}

		if (str != null) {
			str.data = strVec.getFirst().str.data;
		}
		strVec.removeFirst();

		return true;
	}

	// lookup just one!
	static Pair<Boolean, Token> LookUp(LinkedList<Token> strVec, UserType ut, Reserver reserver) throws Exception
	{	
		if (!(strVec.size() >= 2 && false == strVec.getFirst().isComment && false == strVec.get(1).isComment)) {
			if (false == ChkComment(strVec, ut, reserver, 2)) {
				return new Pair<Boolean, Token>( false, null );
			}
		}
		
		
		if (strVec.size() >= 2) {
			return new Pair<Boolean, Token>( true, strVec.get(1) );
		}
		return new Pair<Boolean, Token>( false, null );
	}

	
	public static Pair<Boolean, Integer> Reserve2(BufferedReader inFile, LinkedList<Token> strVec, int num) throws IOException {
		int count = 0;
		WrapString temp = new WrapString();
		ArrayList<WrapString> strVecTemp = new ArrayList<WrapString>(); // need rename! 

		for (int i = 0; i < num && ((temp.data = inFile.readLine()) != null ); ++i) {
			if (temp.data.isEmpty()) { continue; }
			strVecTemp.add(new WrapString(temp.data));
			count++;
		}
		
		// reserve data.
		{
			for (int x = 0; x < strVecTemp.size(); ++x)
			{
				WrapString statement = strVecTemp.get(x);
				int token_first = 0, token_last = 0; // idx of token in statement.data.
				int state = 0;


				for (int i = 0; i < statement.data.length(); ++i) {
					if (0 == state && '\"' == statement.data.charAt(i)) {
						state = 1;
						token_last = i;
					}
					else if (1 == state && '\\' == statement.data.charAt(i - 1) && '\"' == statement.data.charAt(i)) {
						token_last = i;
					}
					else if (1 == state && '\"' == statement.data.charAt(i)) {
						state = 0; token_last = i;
					}

					if (0 == state && '=' == statement.data.charAt(i)) {
						token_last = i - 1;
						if (token_last >= 0 && token_last - token_first + 1 > 0) {
							strVec.addLast(new Token(new WrapString(statement.data.substring(token_first, token_last + 1))));
						}
						strVec.addLast(new Token(new WrapString("=")));
						token_first = i + 1;
					}
					else if (0 == state && Global.isWhiteSpace(statement.data.charAt(i))) { // isspace ' ' \t \r \n , etc... ?
						token_last = i - 1;
						if (token_last >= 0 && token_last - token_first + 1 > 0) {
							strVec.addLast(new Token(new WrapString(statement.data.substring(token_first, token_last + 1))));
						}
						token_first = i + 1;
					}
					else if (0 == state && '{' == statement.data.charAt(i)) {
						token_last = i - 1;
						if (token_last >= 0 && token_last - token_first + 1 > 0) {
							strVec.addLast(new Token(new WrapString(statement.data.substring(token_first, token_last + 1))));
						}
						strVec.addLast(new Token(new WrapString("{")));
						token_first = i + 1;
					}
					else if (0 == state && '}' == statement.data.charAt(i)) {
						token_last = i - 1;
						if (token_last >= 0 && token_last - token_first + 1 > 0) {
							strVec.addLast(new Token(new WrapString(statement.data.substring(token_first, token_last + 1))));
						}
						strVec.addLast(new Token(new WrapString("}")));
						token_first = i + 1;
					}

					if (0 == state && '#' == statement.data.charAt(i)) { // different from load_data_from_file
						token_last = i - 1;
						if (token_last >= 0 && token_last - token_first + 1 > 0) {
							strVec.addLast(new Token(new WrapString(statement.data.substring(token_first, token_last + 1))));
						}
						int j = 0;
						for (j = i; j < statement.data.length(); ++j) {
							if (statement.data.charAt(j) == '\n') // cf) '\r' ?
							{
								break;
							}
						}
						--j; // "before enter key" or "before end"
						
						if (j - i + 1 > 0) {
							strVec.addLast(new Token(new WrapString(statement.data.substring(i, j + 1)), true));
						}
						token_first = j + 2;
						i = token_first - 1;
					}
				}

				if (token_first < statement.data.length())
				{
					strVec.addLast(new Token(new WrapString(statement.data.substring(token_first))));
				}
			}
		}
		
		return new Pair<Boolean, Integer>(count > 0, count);
	}
	
}
