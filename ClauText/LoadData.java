package ClauText;

import java.util.ArrayList;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadData {
	private static final String LEFT = "{";
	private static final String RIGHT = "}";
	private static final String EQ_STR = "=";
	
	private static boolean isState0( long state_reserve)
	{
		return 1 == state_reserve;
	}
	/// core
	public static boolean _LoadData(LinkedList<Token> strVec, Reserver reserver, UserType global) throws Exception // first, strVec.empty() must be true!!
	{
		int state = 0;
		int braceNum = 0;
		long state_reserve=0;
		ArrayList< UserType > nestedUT = new ArrayList<UserType>();
		String var1="", var2="", val="";

		boolean varOn = false;
		
		nestedUT.add(null);
		nestedUT.set(0, global);

		{
			reserver.reserve(strVec);

			while (strVec.isEmpty())
			{
				reserver.reserve(strVec);
				if (
					strVec.isEmpty() &&
					reserver.end()
					) {
					return false; // throw "Err nextToken does not exist"; // cf) or empty file or empty String!
				}
			}
		}

		
		while (false == strVec.isEmpty()) {
			switch (state)
			{
			case 0:
				if (LEFT.equals(Utility.Top(strVec, nestedUT.get(braceNum), reserver))) {
					state = 2;
				}
				else {
					Pair<Boolean, Token> bsPair = Utility.LookUp(strVec, nestedUT.get(braceNum), reserver);

					if (bsPair.first) {
						if (EQ_STR.equals(bsPair.second.str)) {
							Utility.Pop(strVec, var2, nestedUT.get(braceNum), reserver);
							Utility.Pop(strVec, null, nestedUT.get(braceNum), reserver);
							state = 2;
						}
						else {
							if (Utility.Pop(strVec, var1, nestedUT.get(braceNum), reserver)) {
								nestedUT.get(braceNum).AddItem("", var1);
								state = 0;
							}
						}
					}
					else {
						if (Utility.Pop(strVec, var1, nestedUT.get(braceNum), reserver)) {
							nestedUT.get(braceNum).AddItem("", var1);
							state = 0;
						}
					}
				}
				break;
			case 1:
				if (RIGHT.equals(Utility.Top(strVec, nestedUT.get(braceNum), reserver))) {
					Utility.Pop(strVec, null, nestedUT.get(braceNum), reserver);
					state = 0;
				}
				else {
					// syntax error.
					throw new Exception("syntax error 1 ");
				}
				break;
			case 2:
				if (LEFT.equals(Utility.Top(strVec, nestedUT.get(braceNum), reserver))) {
					Utility.Pop(strVec, null, nestedUT.get(braceNum), reserver);

					///
					UserType pTemp= new UserType(var2);
					nestedUT.get(braceNum).AddUserTypeItem(pTemp);

					braceNum++;

					/// new nestedUT
					if (nestedUT.size() == braceNum) /// changed 2014.01.23..
						nestedUT.add(null);

					/// initial new nestedUT.
					nestedUT.set(braceNum, pTemp);
					///
					state = 3;
				}
				else {
					if (Utility.Pop(strVec, val, nestedUT.get(braceNum), reserver)) {
						nestedUT.get(braceNum).AddItem(var2, val);
						var2 = "";
						val = "";

						state = 0;
					}
				}
				break;
			case 3:
				if (RIGHT.equals(Utility.Top(strVec, nestedUT.get(braceNum), reserver))) {
					Utility.Pop(strVec, null, nestedUT.get(braceNum), reserver);

					nestedUT.set(braceNum, null);
					braceNum--;

					state = 0;
				}
				else {
					{
						/// uisng struct
						state_reserve++;
						state = 4;
					}
					//else
					{
						//	throw  "syntax error 2 ";
					}
				}
				break;
			case 4:
				if (LEFT.equals(Utility.Top(strVec, nestedUT.get(braceNum), reserver))) {
					Utility.Pop(strVec, null, nestedUT.get(braceNum), reserver);

					UserType temp = new UserType("");

					nestedUT.get(braceNum).AddUserTypeItem(temp);
					UserType pTemp = temp;

					braceNum++;

					/// new nestedUT
					if (nestedUT.size() == braceNum) /// changed 2014.01.23..
						nestedUT.add(null);

					/// initial new nestedUT.
					nestedUT.set(braceNum, pTemp);
					///
					//}

					state = 5;
				}
				else if (RIGHT.equals(Utility.Top(strVec, nestedUT.get(braceNum), reserver))) {
					Utility.Pop(strVec, null, nestedUT.get(braceNum), reserver);
					state = isState0(state_reserve) ? 0 : 4;
					state_reserve--;

					{
						nestedUT.set(braceNum, null);
						braceNum--;
					}
				}
				else {
					Pair<Boolean, Token> bsPair = Utility.LookUp(strVec, nestedUT.get(braceNum), reserver);
					if (bsPair.first) {
						if (EQ_STR.equals(bsPair.second.str)) {
							// var2
							Utility.Pop(strVec, var2, nestedUT.get(braceNum), reserver);
							Utility.Pop(strVec, null, nestedUT.get(braceNum), reserver); // pass EQ_STR
							state = 6;
						}
						else {
							// var1
							if (Utility.Pop(strVec, var1, nestedUT.get(braceNum), reserver)) {
								nestedUT.get(braceNum).AddItem("", var1);
								var1 = "";

								state = 4;
							}
						}
					}
					else
					{
						// var1
						if (Utility.Pop(strVec, var1, nestedUT.get(braceNum), reserver))
						{
							nestedUT.get(braceNum).AddItem("", var1);
							var1 = "";

							state = 4;
						}
					}
				}
				break;
			case 5:
				if (RIGHT.equals(Utility.Top(strVec, nestedUT.get(braceNum), reserver))) {
					Utility.Pop(strVec, null, nestedUT.get(braceNum), reserver);

					//if (flag1 == 0) {
					nestedUT.set(braceNum, null);
					braceNum--;
					// }
					//
					state = 4;
				}

				else {
					int idx = -1;
					int num = -1;

					
					{
						/// uisng struct
						state_reserve++;
						state = 4;
					}
					//else
					{
						//	throw "syntax error 4  ";
					}
				}
				break;
			case 6:
				if (LEFT.equals(Utility.Top(strVec, nestedUT.get(braceNum), reserver))) {
					Utility.Pop(strVec, null, nestedUT.get(braceNum), reserver);

					///
					{
						UserType pTemp = new UserType(var2);
						nestedUT.get(braceNum).AddUserTypeItem(pTemp);
						var2 = "";
						braceNum++;

						/// new nestedUT
						if (nestedUT.size() == braceNum) /// changed 2014.01.23..
							nestedUT.add(null);

						/// initial new nestedUT.
						nestedUT.set(braceNum, pTemp);
					}
					///
					state = 7;
				}
				else {
					if (Utility.Pop(strVec, val, nestedUT.get(braceNum), reserver)) {

						nestedUT.get(braceNum).AddItem(var2, val);
						var2 = ""; val = "";
						if (strVec.isEmpty())
						{
							//
						}
						else if (RIGHT.equals(Utility.Top(strVec, nestedUT.get(braceNum), reserver))) {
							Utility.Pop(strVec, null, nestedUT.get(braceNum), reserver);

							{
								state = isState0(state_reserve) ? 0 : 4;
								state_reserve--;

								{
									nestedUT.set(braceNum, null);
									braceNum--;
								}
							}
							{
								//state = 4;
							}
						}
						else {
							state = 4;
						}
					}
				}
				break;
			case 7:
				if (RIGHT.equals(Utility.Top(strVec, nestedUT.get(braceNum), reserver))) {
					Utility.Pop(strVec, null, nestedUT.get(braceNum), reserver);
					//

					nestedUT.set(braceNum, null);
					braceNum--;
					//
					state = 4;
				}
				else {
					int idx = -1;
					int num = -1;
					
					{
						/// uisng struct
						state_reserve++;

						state = 4;
					}
					//else
					{
						//throw "syntax error 5 ";
					}
				}
				break;
			default:
				// syntax err!!

				throw new Exception("syntax error 6 ");
			}

			if (strVec.size() < 10) {
				reserver.reserve(strVec);

				while (strVec.isEmpty()) // (strVec.empty())
				{
					reserver.reserve(strVec);
					if (
						strVec.isEmpty() &&
						reserver.end()
						) {
						// throw "Err nextToken does not exist2";
						break;
					}
				}
			}
		}
		if (state != 0) {
			throw new Exception("error final state is not 0!  : " + Integer.toString(state));
		}
		if (braceNum != 0) {
			throw new Exception("chk braceNum is " + Integer.toString(braceNum));
		}
		
		return true;
	}

	public static boolean LoadDataFromFile(String fileName, UserType global) throws IOException /// global should be empty
	{
		UserType globalTemp=null;
		FileReader reader=null;
		BufferedReader inFile=null;
			
		try{
			reader = new FileReader(fileName);
			inFile = new BufferedReader(reader);
			
			globalTemp = new UserType(global);
			LinkedList<Token> strVec = new LinkedList<Token>();;
	
			InFileReserver ifReserver = new InFileReserver(inFile);
	
			ifReserver.setNum(100000);
			
			// cf) empty file..
			if (false == _LoadData(strVec, ifReserver, globalTemp))
			{
				return true;
			}
	
			inFile.close();
			reader.close();
		}
		catch(Exception e) { 
			if(inFile != null) inFile.close();
			if(reader!= null) reader.close();
			
			return false; 
		}
		
		global = globalTemp;
		return true;
	}


	public static boolean LoadDataFromString(String str, UserType ut) {
		UserType utTemp = new UserType();
		LinkedList<Token>strVec = new LinkedList<Token>();

		String statement=str;int token_first=0,token_last=0; // idx of token in
																// statement.
		int state=0;

		for(int i=0;i<statement.length();++i){
			if(0==state&&'\"'==statement.charAt(i)){
				// token_last = i - 1;
				// if (token_last >= 0 && token_last - token_first + 1 > 0) {
				// strVec.emplace_back(statement.substr(token_first, token_last -
				// token_first + 1));
				// }
				state=1;
				// token_first = i;
				token_last=i;
			}
			else if(1==state && '\\'==statement.charAt(i - 1) && '\"'==statement.charAt(i)) {
				token_last=i;
			}
			else if(1==state &&'\"'==statement.charAt(i)) {
				state=0;
				token_last=i;
	
			// strVec.emplace_back(statement.substr(token_first, token_last -
			// token_first + 1));
			// token_first = i + 1;
			}
	
			if(0==state && '='==statement.charAt(i)) {
				token_last=i-1;
				if(token_last >= 0 && token_last-token_first+1 > 0){
					strVec.add(new Token(statement.substring(token_first,token_last+1)));
				}
				strVec.add(new Token("="));
				token_first = i + 1;
			}
			else if(0==state && Global.isWhiteSpace(statement.charAt(i))) { // isspace																																									// etc...// ?
				token_last=i-1;
				if(token_last >= 0 && token_last - token_first + 1 > 0) {
					strVec.add(new Token(statement.substring(token_first, token_last - token_first + 1)));
				}
				token_first = i + 1;
			}
			else if(0 == state && '{' == statement.charAt(i)) {
				token_last = i - 1; 
				if(token_last >= 0 && token_last - token_first + 1 > 0){
					strVec.add(new Token(statement.substring(token_first, token_last+1)));
				}
				strVec.add(new Token("{"));
				token_first = i + 1;
			}
			else if(0 == state && '}' == statement.charAt(i)) {
				token_last = i - 1;
				if(token_last >= 0 && token_last - token_first + 1 > 0) {
					strVec.add(new Token(statement.substring(token_first, token_last - token_first + 1)));
				}
				strVec.add(new Token("}"));
				token_first=i+1;
			}
	
			if(0 == state && '#' == statement.charAt(i)){ // different from load_data_from_file
				token_last=i-1;
				if(token_last >= 0 && token_last - token_first + 1 > 0) {
					strVec.add(new Token(statement.substring(token_first, token_last + 1)));
				}
				int j=0;
				for(j = i; j < statement.length(); ++j) {
					if(statement.charAt(j) == '\n') // cf)																																								// ?
					{break;}
				}
				--j; // "before enter key" or "before end"
	
				if(j - i + 1 > 0) { 
					strVec.add(new Token(statement.substring(i,j + 1), true));
				}
				token_first=j+2;
				i=token_first-1;
			}
		}

		if(token_first < statement.length()){
			strVec.add(new Token(statement.substring(token_first)));
		}

		try{
		// empty String!
			NoneReserver nonReserver = new NoneReserver();
			if(false==_LoadData(strVec,nonReserver,utTemp)){
				return true;
			}
		}
		catch(Exception e) {
			return false;
		}
		ut = utTemp;
		return true;
	}

}
