
package ClauText;

import java.util.LinkedList; // for test.
import java.util.ListIterator;

public class ClauTextMain {

	public static void main(String[] args) {
		LinkedList<String> test = new LinkedList<String>();
		
		test.add("0");
		test.add("1");
		test.add("2");
		test.add("3");
		test.add("4");
		test.add("5");
		
		ListIterator x = test.listIterator(1);
		
		while(x.hasNext()) {
			System.out.println(x.next());
		}
		
		
		System.out.println("Hello world!");
	}

}
