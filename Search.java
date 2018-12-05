/*Keisha Quirimit
 * kquirimi
 * CMPS12B-02
 * January 28, 2018
 * searches for target word in an input file using binary search
 * Search.java
 */

import java.io.*;
import java.util.Scanner;

public class Search {

	public static void mergeSort(String[] word, int[] lineNum, int p, int r) {
		int q;
		if (p < r) {
			q = (p + r) / 2;
			mergeSort(word, lineNum, p, q);
			mergeSort(word, lineNum, q + 1, r);
			merge(word, lineNum, p, q, r);

		}
	}

	public static void merge(String[] word, int[] lineNum, int p, int q, int r) {
		int n1 = q - p + 1;
		int n2 = r - q;
		String[] L = new String[n1];
		String[] R = new String[n2];
		int[] leftLineNum = new int[n1];
		int[] rightLineNum = new int[n2];
		int i, j, k;
		
		//fills "left" array
		for (i = 0; i < n1; i++) {
			L[i] = word[p + i];
			leftLineNum[i] = lineNum[p+i];
		}
		//fills "right" array
		for (j = 0; j < n2; j++) {
			R[j] = word[q + j + 1];
			rightLineNum[j] = lineNum[q+j+1];
		}

		i = 0;
		j = 0;
		//fills and alphabetizes the word array
		for (k = p; k <= r; k++) {
			if (i < n1 && j < n2) {
				if (L[i].compareTo(R[j])<0) {
					word[k] = L[i];
					//moves element in lineNum to same position to keep track of original file line number
					lineNum[k] = leftLineNum[i];
					i++;
				} else {
					word[k] = R[j];
					//moves element in lineNum to same position to keep track of original file line number
					lineNum[k] = rightLineNum[j];
					j++;
				}
			} else if (i < n1) {
				word[k] = L[i];
				//moves element in lineNum to same position to keep track of original file line number
				lineNum[k] = leftLineNum[i];
				i++;
			} else { // j<n2
				word[k] = R[j];
				//moves element in lineNum to same position to keep track of original file line number
				lineNum[k] = rightLineNum[j];
				j++;
			}
		}
	}

	public static int binarySearch(String[] word, int p, int r, String target)
	{
		int q;
		if(p>r)
		{
			return -1;
		}
		else
		{
			 q = (p+r)/2;
	         if(target.equals(word[q])){//return index if target found at index
	            return q;
	         }else if(target.compareTo(word[q])<0){//if target is lexicographically before  
	            return binarySearch(word, p, q-1, target);
	         }else{ // target.compareTo(word[q])>0
	            return binarySearch(word, q+1, r, target);
	         }
		}
	}
	
	
	public static void main(String[] args) throws IOException{
		Scanner in = null;
		String line = null;//used to read each line in the infile
		String[] words = null;//holds each word in the infile
		int[] lineNumber = null;//keeps track of line number in the infile
		int lineCtr = 0;
		int index;
		
		//checks there is at least 2 command line arguments 
		if(args.length<2)
		{
			System.out.println("Usage: Search <input file> target1 [target2...]");
			System.exit(1);		
		}
		
		//open file
		in = new Scanner(new File(args[0]));
		
		//count number of lines from infile
		while(in.hasNextLine()) {
			lineCtr++;
			line = in.nextLine();
		}
		in.close();
		
		in = new Scanner(new File(args[0]));
		
		//initialize size of words and lineNumber arrays to number of lines in file
		words = new String[lineCtr];
		lineNumber = new int[lineCtr];
		
		//fill arrays
		for(int i=0; i<lineCtr;i++)
		{
			words[i] = in.nextLine();
			lineNumber[i] = i+1;
		}
		in.close();
		
		//sorts words array in order to utilize binary search
		mergeSort(words, lineNumber, 0, words.length-1);
		

		//performs binary search for each target listed in args
		for(int i=1;i<args.length;i++)
		{
			index = binarySearch(words, 0, words.length-1, args[i]);
			if(index==-1)//target not found
				System.out.println("" + args[i] + " not found");
			else
				System.out.println("" + args[i] + " found on line " + lineNumber[index]);
		}
		
	}//end of main

}//end of class
