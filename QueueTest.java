/* Keisha Quirimit
 * kquirimi
 * CMPS12B-02
 * February 26, 2018
 * testing methods of Queue work correctly
 * QueueTest.java
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.*;

public class QueueTest {

	public static void main(String[] args) throws IOException {
		Scanner in = null;
		PrintWriter out = null;
		String line = "";
		String[] times;
		int numJobs = 0;

		if (args.length < 1) {
			System.out.println("Usage: QueueTest <input file>");
			System.exit(1);
		}

		// open files
		in = new Scanner(new File(args[0]));
		PrintWriter report = new PrintWriter(new FileWriter(args[0] + ".rpt"));

		line = in.nextLine();
		numJobs = Integer.parseInt(line);

		Queue backup = new Queue();
		Job add = null;
		if (backup.isEmpty())
			report.println("Jobs Queue is empty: yes");
		
		// read infile and fill in jobs queue
		while (in.hasNextLine()) {
			add = getJob(in);
			backup.enqueue((Job)add);
		}
		//Job test = (Job)backup.dequeue();
		//report.println(((Job)backup.find(1)).getArrival());
		
		report.println(backup);
		report.println("Job Queue has " + backup.length() + " element(s)");
		Object removed = (Job)backup.dequeue();
		report.println("Removed job: " + removed.toString());
		report.println(backup);		
		
		in.close();
		report.close();
	}
	
	public static Job getJob(Scanner in) {
	      String[] s = in.nextLine().split(" ");
	      int a = Integer.parseInt(s[0]);
	      int d = Integer.parseInt(s[1]);
	      return new Job(a, d);
}

}
