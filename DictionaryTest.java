/*Keisha Quirimit
 * kquirimi
 * CMPS12B-02
 * February 6, 2018
 * tests methods of Dictionary are functioning correctly
 * DictionaryTest.java
 */
public class DictionaryTest {
	public static void main(String[] args) {
		Dictionary book = new Dictionary();
		System.out.println("Dictionary created. Current size: "+ book.size());
		System.out.print("Dictionary is empty: ");
		if(book.isEmpty()) //checks if Dictionary is empty
			System.out.println("true");
		else
			System.out.println("false");
		
		//test inserting pairs into Dictionary
		book.insert("1", "apple");
		book.insert("2", "boy");
		book.insert("3", "cat");
		//book.insert("2", "ball"); //test DuplicateKeyException
		
		System.out.println("Current size: "+ book.size()); //checks book.size() has been updated after filling Dictionary
		System.out.print("Dictionary is empty: ");
		if(book.isEmpty())
			System.out.println("true");
		else
			System.out.println("false");
		
		//tests lookup() 
		System.out.println("Lookup at 3: " + book.lookup("3"));
		System.out.println("Lookup at 1: " + book.lookup("1"));
		System.out.println("Lookup at 0: " + book.lookup("0"));
		
		//book.delete("4"); //test KeyNotFoundException
		/*
		book.delete("1");
		System.out.println(book);
		book.delete("3");
		System.out.println(book);
		*/
		book.delete("2");
		//System.out.println(book);
		book.insert("2", "backpack");
		
		System.out.println(book);
		
		book.makeEmpty();
		System.out.println(book);
		System.out.print("Dictionary is empty: ");
		if(book.isEmpty())
			System.out.println("true");
		else
			System.out.println("false");
		
		}

}
