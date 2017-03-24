package ClauText;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import ClauText.UserType;

public class Utility {
	public static Pair<Boolean, Integer> Reserve2(BufferedReader inFile, LinkedList<Token> strVec, int num) throws IOException {
		int count = 0;
		String temp;
		ArrayList<String> strVecTemp = new ArrayList<String>(); // need rename! 

		for (int i = 0; i < num && ((temp = inFile.readLine()) != null ); ++i) {
			if (temp.isEmpty()) { continue; }
			strVecTemp.add(temp);
			count++;
		}
		
		// reserve data.
		{
			for (int x = 0; x < strVecTemp.size(); ++x)
			{
				String statement = strVecTemp.get(x);
				int token_first = 0, token_last = 0; // idx of token in statement.
				int state = 0;


				for (int i = 0; i < statement.length(); ++i) {
					if (0 == state && '\"' == statement.charAt(i)) {
						state = 1;
						token_last = i;
					}
					else if (1 == state && '\\' == statement.charAt(i - 1) && '\"' == statement.charAt(i)) {
						token_last = i;
					}
					else if (1 == state && '\"' == statement.charAt(i)) {
						state = 0; token_last = i;
					}

					if (0 == state && '=' == statement.charAt(i)) {
						token_last = i - 1;
						if (token_last >= 0 && token_last - token_first + 1 > 0) {
							strVec.addLast(new Token(statement.substring(token_first, token_last + 1)));
						}
						strVec.addLast(new Token("="));
						token_first = i + 1;
					}
					else if (0 == state && Global.isWhiteSpace(statement.charAt(i))) { // isspace ' ' \t \r \n , etc... ?
						token_last = i - 1;
						if (token_last >= 0 && token_last - token_first + 1 > 0) {
							strVec.addLast(new Token(statement.substring(token_first, token_last + 1)));
						}
						token_first = i + 1;
					}
					else if (0 == state && '{' == statement.charAt(i)) {
						token_last = i - 1;
						if (token_last >= 0 && token_last - token_first + 1 > 0) {
							strVec.addLast(new Token(statement.substring(token_first, token_last + 1)));
						}
						strVec.addLast(new Token("{"));
						token_first = i + 1;
					}
					else if (0 == state && '}' == statement.charAt(i)) {
						token_last = i - 1;
						if (token_last >= 0 && token_last - token_first + 1 > 0) {
							strVec.addLast(new Token(statement.substring(token_first, token_last + 1)));
						}
						strVec.addLast(new Token("}"));
						token_first = i + 1;
					}

					if (0 == state && '#' == statement.charAt(i)) { // different from load_data_from_file
						token_last = i - 1;
						if (token_last >= 0 && token_last - token_first + 1 > 0) {
							strVec.addLast(new Token(statement.substring(token_first, token_last + 1)));
						}
						int j = 0;
						for (j = i; j < statement.length(); ++j) {
							if (statement.charAt(j) == '\n') // cf) '\r' ?
							{
								break;
							}
						}
						--j; // "before enter key" or "before end"
						
						if (j - i + 1 > 0) {
							strVec.addLast(new Token(statement.substring(i, j + 1), true));
						}
						token_first = j + 2;
						i = token_first - 1;
					}
				}

				if (token_first < statement.length())
				{
					strVec.addLast(new Token(statement.substring(token_first)));
				}
			}
		}
		
		return new Pair<Boolean, Integer>(count > 0, count);
	}
	
}
