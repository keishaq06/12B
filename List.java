/* Keisha Quirimit
 * kquirimi
 * CMPS12M-02
 * March 9, 2018
 * generic type List
 * List.java
 */
@SuppressWarnings("overrides")
public class List<T> implements ListInterface<T> {

	// private inner Node class
	private class Node {
		T item;
		Node next;

		Node(T x) {
			item = x;
			next = null;
		}
	}

	private Node head;
	private int numItems;

	//constructor
	public List() {
		head = null;
		numItems = 0;
	}

	// private helper function
	private Node find(int index) {
		Node N = head;
		for (int i = 1; i < index; i++) {
			N = N.next;
		}
		return N;
	}

	// ADT operations

	//returns true if List is empty
	public boolean isEmpty() {
		return (numItems == 0);
	}

	//returns size of List
	public int size() {
		return numItems;
	}

	//returns item at specified index
	public T get(int index) throws ListIndexOutOfBoundsException {
		if (index < 1 || index > numItems) {
			throw new ListIndexOutOfBoundsException("IntegerList Error: get() called on invalid index: " + index);
		}
		Node N = find(index);
		return N.item;
	}

	//adds a new Node at the specified index
	public void add(int index, T newItem) throws ListIndexOutOfBoundsException {

		if (index < 1 || index > (numItems + 1)) {
			throw new ListIndexOutOfBoundsException("IntegerList Error: add() called on invalid index: " + index);
		}
		if (index == 1) {
			Node N = new Node(newItem);
			N.next = head;
			head = N;
		} else {
			Node P = find(index - 1); // at this point index >= 2
			Node C = P.next;
			P.next = new Node(newItem);
			P = P.next;
			P.next = C;
		}
		numItems++;
	}

	//removes Node at specified index
	public void remove(int index) throws ListIndexOutOfBoundsException {
		if (index < 1 || index > numItems) {
			throw new ListIndexOutOfBoundsException("IntegerList Error: remove() called on invalid index: " + index);
		}
		if (index == 1) {
			Node N = head;
			head = head.next;
			N.next = null;
		} else {
			Node P = find(index - 1);
			Node N = P.next;
			P.next = N.next;
			N.next = null;
		}
		numItems--;
	}

	//removes all elements in List
	public void removeAll() {
		head = null;
		numItems = 0;
	}

	//returns String representation of the List
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Node N = head;

		for (; N != null; N = N.next) {
			sb.append(N.item).append(" ");
		}
		return new String(sb);
	}

	//returns true if the two Lists are equal
	@SuppressWarnings("unchecked")
	public boolean equals(Object rhs) {
		List<T> R = null;

		if (this.getClass() == rhs.getClass()) {
			R = (List<T>) rhs;
			if (this.size() != R.size()) //if the two Lists aren't the same size
				return false;
			else if (this.size() == 0 && R.size() ==0) { //if both Lists are empty
				return true;
			} else {
				for (int i = 1; i <= this.size(); i++) {
					if (!(this.get(i)).equals(R.get(i))) {
						return false;
					}
				}
			}

		}
		return true;
	}

}
