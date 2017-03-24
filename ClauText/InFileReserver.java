package ClauText;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class InFileReserver extends Reserver
{
	private BufferedReader InFile;
	private int Num;
	public InFileReserver(BufferedReader inFile)
	{
		InFile = inFile;
		Num = 1;
	}
	public boolean end() throws IOException {  
		InFile.mark(1);
		int i = InFile.read();
		InFile.reset();
		
		return i < 0; 
	}
	
	public boolean reserve(LinkedList<Token> strVec) throws IOException
	{
		return Utility.Reserve2(InFile, strVec, Num).second > 0;
	}
	
	public void setNum(int val)
	{
		this.Num = val;
	}
	public int getNum() { return this.Num; }
}
