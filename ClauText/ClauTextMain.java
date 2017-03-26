
package ClauText;

import java.io.IOException;
import java.util.LinkedList; // for test.
import java.util.ListIterator;

public class ClauTextMain {

	public static void main(String[] args) throws IOException {
		UserType global = new UserType();
		
		long startTime = System.currentTimeMillis();

		if(LoadData.LoadDataFromFile("C:/Users/wiz/Documents/Java/JClauText/input.eu4", global))
		{
			long endTime = System.currentTimeMillis();
	         
			// Total time
			long lTime = endTime - startTime;
			System.out.println("TIME : " + lTime + "(ms)");

			System.out.println("SUCCESS");
		}
		
		System.out.println("TEST END");
	}

}
