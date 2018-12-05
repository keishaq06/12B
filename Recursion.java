/*Keisha Quirimit
 * kquirimi
 * CMPS12B-02
 * January 18, 2018
 * reverses array and finds index of min/max values of the array
 * Recursion.java
 */

public class Recursion {

	static void reverseArray1(int[] X, int n, int[] Y) {
		if (n == 0)
			return;
		else // if n>0
		{
			int num = X[X.length - n]; // copies leftmost n element
			Y[n - 1] = num; // places this copied element in rightmost n position
			reverseArray1(X, n - 1, Y);
		}

	}

	static void reverseArray2(int[] X, int n, int[] Y) {
		if (n == 0)
			return;
		else // if n>0
		{
			int num = X[n - 1]; // copies rightmost n element
			Y[Y.length - n] = num; // places copied element in leftmost n position
			reverseArray2(X, n - 1, Y);
		}
	}

	static void reverseArray3(int[] X, int i, int j) {
		if (i <= X.length / 2 && j >= X.length / 2) { // checks if i is on the left half of the array and j on the right
														// half
			int temp = X[i];
			X[i] = X[j]; // swaps "left half" element with "right half" element
			X[j] = temp;
			reverseArray3(X, i + 1, j - 1);
		}
	}

	// returns index of the maximum element in the array
	static int maxArrayIndex(int[] X, int p, int r) {
		int q;

		int index1;
		int index2;

		if (p == r) // if subarray size is 1
			return p;
		else // p<r
		{
			q = (p + r) / 2; // finds midpoint
			index1 = maxArrayIndex(X, p, q); // holds index of max element in the "left" subarray
			index2 = maxArrayIndex(X, q + 1, r); // holds index of max element in the "right" subarray
			return mergeMax(X, index1, index2); // compares max values of left and right subarrays
		}
	}

	// compares max values of left and right subarrays
	static int mergeMax(int[] X, int leftIndex, int rightIndex) {
		if (X[leftIndex] > X[rightIndex]) {
			return leftIndex;
		} else // if X[leftIndex] <= X[rightIndex]
		{
			return rightIndex;
		}

	}

	// returns index of minimum element in the array
	static int minArrayIndex(int[] X, int p, int r) {
		int q;

		int index1;
		int index2;

		if (p == r) // subarray size is 1
			return p;
		else // p<r
		{
			q = (p + r) / 2; // finds midpoint
			index1 = minArrayIndex(X, p, q); // holds index of min value in left subarray
			index2 = minArrayIndex(X, q + 1, r); // holds index of min value in right subarray
			return mergeMin(X, index1, index2); // compares min values of left and right subarrays
		}

	}

	// compares minimum values of left and right subarrays
	static int mergeMin(int[] X, int leftIndex, int rightIndex) {
		if (X[leftIndex] < X[rightIndex]) {
			return leftIndex;
		} else // if X[leftIndex] >= X[rightIndex]
		{
			return rightIndex;
		}

	}

	public static void main(String[] args) {
		int[] A = { -1, 2, 6, 12, 9, 2, -5, -2, 8, 5, 7 };
		int[] B = new int[A.length];
		int[] C = new int[A.length];
		int minIndex = minArrayIndex(A, 0, A.length - 1);
		int maxIndex = maxArrayIndex(A, 0, A.length - 1);

		for (int x : A)
			System.out.print(x + " ");
		System.out.println();

		System.out.println("minIndex = " + minIndex);
		System.out.println("maxIndex = " + maxIndex);

		reverseArray1(A, A.length, B);
		for (int x : B)
			System.out.print(x + " ");
		System.out.println();

		reverseArray2(A, A.length, C);
		for (int x : C)
			System.out.print(x + " ");
		System.out.println();

		reverseArray3(A, 0, A.length - 1);
		for (int x : A)
			System.out.print(x + " ");
		System.out.println();

	}
}
