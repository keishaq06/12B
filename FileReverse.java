/*Keisha Quirimit
 * kquirimi
 * CMPS12M-02
 * January 23, 2018
 * reads tokens from a file, then prints reversed tokens in an out file
 * FileReverse.java
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.*;
import java.util.Scanner;


public class FileReverse {

	public static void main(String[] args) throws IOException {
		Scanner in= null;
		PrintWriter out = null;
		String line = null;
		String[] tokens;
		String token = "";
		
		//check number of command line arguments is at least 2
		if(args.length <2)
		{
			System.out.println("Usage: FileReverse <input file> <output file>");
			System.exit(1);
		}
		
		//open files
		in= new Scanner(new File(args[0]));
		out = new PrintWriter(new FileWriter(args[1]));
		
		//read lines from in, writes lines to out
		while(in.hasNextLine()) {
			

	         // trim leading and trailing spaces, then add one trailing space so 
	         // split works on blank lines
	         line = in.nextLine().trim() + " "; 

	         // split line around white space 
	         tokens = line.split("\\s+"); 
	         
	         //reverses each token in the line
	         for(int j=0; j<tokens.length;j++)
	         {
	        	 token = stringReverse(tokens[j], tokens[j].length());
	        	 tokens[j] = token; 
	         
	         }
			
	         //prints reversed tokens in out file
			for(String s: tokens)
			{
				out.println(s);
			}
			
		}
		//close files
		in.close();
		out.close();
	} 
	

	//reverse String by returning character at index n, from the left
	public static String stringReverse(String s, int n)
	{
		if(n==1)
			return "" + s.charAt(0);
		else //n>1
		{
			return "" + s.charAt(n-1) + stringReverse(s, n-1);
		}
		
	}
}
