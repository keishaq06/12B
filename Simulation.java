/* Keisha Quirimit
 * kquirimi
 * CMPS12B-02
 * February 26, 2018
 * using queues, performs simulation with m jobs and 1 to m - 1 processors
 * Simulation.java
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Simulation {

	public static void main(String[] args) throws IOException {
		Scanner in = null;
		int numJobs = 0; 

		// checks input file name is given
		if (args.length < 1) {
			System.out.println("Usage: QueueTest <input file>");
			System.exit(1);
		}

		// open files
		in = new Scanner(new File(args[0]));
		PrintWriter report = new PrintWriter(new FileWriter(args[0] + ".rpt"));
		PrintWriter tracefile = new PrintWriter(new FileWriter(args[0] + ".trc"));

		tracefile.println("Trace file: " + args[0] + ".trc");
		report.println("Report file: " + args[0] + ".rpt");

		// determines number of jobs from first line of input file
		numJobs = Integer.parseInt(in.nextLine());

		Queue backup = new Queue(); // holds jobs in original input file order
		Job add = null;
		// reads input file and fills backup queue with jobs
		for (int i = 0; i < numJobs; i++) {
			add = getJob(in);
			backup.enqueue(add);
		}

		tracefile.println("" + numJobs + " Jobs:");
		tracefile.println(backup);
		tracefile.println();
		report.println("" + numJobs + " Jobs:");
		report.println(backup);
		report.println();
		report.println("***********************************************************");

		int totalWait = 0;
		int maxWait = 0;
		double avgWait = 0;
		int time = 0;
		Job frontStore = null;
		int arrivalTime = 0;
		int finishTime = 0;
		int numProcessors = 0; // number of processors
		Queue[] processors; // holds processors for jobs

		// simulation for 1 to numJobs-1 processors
		for (int n = 1; n < numJobs; n++) {
			time = 0;
			processors = new Queue[n + 1];
			numProcessors = n;
			processors[0] = copyBackup(backup);// first processor in array of Queues holds the original job order

			// create processors
			for (int j = 1; j < n + 1; j++) {
				processors[j] = new Queue();
			}

			tracefile.println("*****************************");
			if (numProcessors == 1) {
				tracefile.println("1 processor:");
			} else {
				tracefile.println(numProcessors + " processors:");
			}

			// run while there are unprocessed jobs (jobs in processors[0] still remaining)
			while (numJobs > 0) {
				// check for any finishing jobs
				Job front = null;
				for (int i = 1; i <= numProcessors; i++) {
					if (!processors[i].isEmpty()) {
						front = (Job) processors[i].peek();
						if (front.getFinish() == time) {
							processors[0].enqueue((Job) processors[i].dequeue()); // remove from processor and put into storage queue							// storage queue
							numJobs--; // decrease number of jobs that still need to be processed
							if (!processors[i].isEmpty()) {  //if processor still not empty after dequeue
								front = (Job) processors[i].peek();
								front.computeFinishTime(time); //compute finish time for Job currently at the front of the queue
							}
						}
					}
				}

				// assign arriving jobs
				if (!processors[0].isEmpty()) { //checks if there are still jobs to assign
					Job assign = (Job) (processors[0].peek());
					int minIndex = 0;
					arrivalTime = assign.getArrival();
					while (arrivalTime == time) { //if job arrives at this time
						minIndex = minimumLength(processors);// find shortest queue
						processors[minIndex].enqueue((Job) assign); // assigns job to shortest found processor
						processors[0].dequeue(); // remove from storage queue once assigned to a processor
						if ((Job) processors[minIndex].peek() == assign) { // check if newly assigned job is in front of
																			// processor
							((Job) processors[minIndex].peek()).computeFinishTime(time); // compute finish time
						}
						if (processors[0].length() > 0) { //check if there are jobs still need to be assigned
							assign = (Job) (processors[0].peek());
							arrivalTime = assign.getArrival();
						} else // no more jobs left in storage queue
							break;
					}
				}

				// print status of processors
				tracefile.println("time=" + time);
				tracefile.println("0: " + processors[0]);
				for (int i = 1; i <= numProcessors; i++) {
					tracefile.print("" + i + ": ");
					tracefile.println(processors[i]);
				}
				tracefile.println();

				// update time
				if (lowestFinishTime(processors) == -1) { // check if processors are empty
					frontStore = (Job) processors[0].peek();
					arrivalTime = frontStore.getArrival();
					time = arrivalTime;
				} else if (processors[0].isEmpty()) { // check if storage queue is empty
					finishTime = lowestFinishTime(processors);
					time = finishTime;
				} else if (((Job) processors[0].peek()).getFinish() != -1) { // check for finished jobs
					finishTime = lowestFinishTime(processors);
					time = finishTime;
				} else { // able to check both storage queue and processors
					frontStore = (Job) processors[0].peek();
					arrivalTime = frontStore.getArrival();
					finishTime = lowestFinishTime(processors);
					if (arrivalTime <= finishTime)
						time = arrivalTime;
					else // finishTime is the min value
						time = finishTime;
				}

			} // end of while loop for unprocessed jobs remaining

			// calculate total, max, and average wait times
			totalWait = totalWaitTime(processors[0]);
			maxWait = maxWaitTime(processors[0]);
			avgWait = averageWaitTime(processors[0]);

			// print times to report file
			if (numProcessors == 1) {
				report.print("1 processor: ");

			} else {
				report.print(numProcessors + " processors: ");
			}
			report.print("totalWait=" + totalWait + ", ");
			report.print("maxWait=" + maxWait + ", ");
			report.printf("averageWait=%.2f", avgWait);
			report.println();

			numJobs = backup.length(); //reset number of jobs left to be processed

		} // end of for loop for 1 to numJobs-1 processors

		// close files
		in.close();
		report.close();
		tracefile.close();

	}// end of main

	// create Job object from reading file
	public static Job getJob(Scanner in) {
		String[] s = in.nextLine().split(" ");
		int a = Integer.parseInt(s[0]);
		int d = Integer.parseInt(s[1]);
		return new Job(a, d);
	}

	//method to create storage queue that is a copy of backup queue (original read from input file)
	public static Queue copyBackup(Queue q) {
		Queue copy = new Queue();
		Job temp = null;
		for (int i = 0; i < q.length(); i++) {
			temp = (Job) q.dequeue();
			copy.enqueue(new Job(temp.getArrival(), temp.getDuration()));
			q.enqueue(temp); 
		}
		return copy;

	}

	// find lowest finish time out of all processors
	public static int lowestFinishTime(Queue[] p) {
		int min = -1;
		for (int i = 1; i < p.length; i++) { // set min to first job in first non empty queue
			if (!p[i].isEmpty()) {
				min = ((Job) p[i].peek()).getFinish();
				break;
			}
			// else keep searching for first
		}
		// find lowest finish time only if processors are not empty
		for (int j = 1; j < p.length; j++) {
			if (!p[j].isEmpty()) {
				if (((Job) p[j].peek()).getFinish() < min) {
					min = ((Job) p[j].peek()).getFinish();
				}
			}
		}
		return min; // returns -1 is processors are empty
	}

	// calculates average wait time for m jobs
	public static double averageWaitTime(Queue q) {
		int length = q.length();
		double total = totalWaitTime(q);
		double average = total / length;
		return average;
	}

	// calculates total wait time for m jobs
	public static int totalWaitTime(Queue q) {
		Job j = null;
		int total = 0;
		for (int i = 0; i < q.length(); i++) {
			j = (Job) q.dequeue();
			q.enqueue(j);
			total += (j.getWaitTime());
		}
		return total;
	}

	// calculates maximum wait time for m jobs
	public static int maxWaitTime(Queue q) {
		Job j = (Job) q.peek();
		int max = j.getWaitTime();
		for (int i = 0; i < q.length(); i++) {
			j = (Job) q.peek();
			if (j.getWaitTime() > max) {
				max = j.getWaitTime();
			}
			j = (Job) q.dequeue();
			q.enqueue(j);
		}
		return max;
	}

	// returns smallest index of queue with shortest length
	public static int minimumLength(Queue[] p) {
		int min = p[1].length();
		int tempIndex = 1; // hold smallest index of shortest Queue
		for (int i = 1; i < p.length; i++) { // traverse through Queue[] to find queue with minimum length
			if (p[i].length() < min) {
				min = p[i].length();
				tempIndex = i;
			}
		}
		return tempIndex;
	}
}
