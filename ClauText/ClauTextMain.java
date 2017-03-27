
package ClauText;

import java.io.IOException;
import java.util.LinkedList; // for test.
import java.util.ListIterator;

public class ClauTextMain {

	public static void main(String[] args) throws Exception {
		{
			WrapString str1 = new WrapString("A");
			WrapString str2 = new WrapString("B");
			
			System.out.println(str1.data + " " + str2.data);
			str1.swap(str2);
			System.out.println(str1.data + " " + str2.data);
			System.out.println();
		}
		
		UserType global = new UserType();
		
		long startTime = System.currentTimeMillis();
		
		//C:/Users/wiz/Documents/Java/JClauText/input.eu4

		if(LoadData.LoadDataFromFile(new WrapString("C:/Users/wiz/Desktop/Clau/ClauText/input.eu4"), global))
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
