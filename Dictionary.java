/* Keisha Quirimit
 * kquirimi
 * CMPS12M-02
 * March 16, 2018
 * DictionaryADT- Binary Search Tree implementation
 */
public class Dictionary implements DictionaryInterface {
	// --------private inner Node class----------------
	private class Node {
		String key;
		String value;
		Node left;
		Node right;

		public Node(String k, String v) {
			key = k;
			value = v;
			left = null;
			right = null;
		}
	}

	// --------private functions ------------------------
	private Node findKey(Node R, String k) {
		if (R == null || k.equals(R.key))
			return R;
		if (k.compareTo(R.key) < 0)
			return findKey(R.left, k);
		else // k.compareTo(R.key)>0
			return findKey(R.right, k);
	}
	//returns the parent of N in the subtree rooted at R or returns null
	private Node findParent(Node N, Node R) {
		Node P = null;
		if (N != R) {
			P = R;
			while (P.left != N && P.right != N) {
				if (N.key.compareTo(P.key) < 0)
					P = P.left;
				else
					P = P.right;
			}
		}
		return P;
	}
	//returns the leftmost Node in the subtree rooted at R, or null if R is null
	private Node findLeftMost(Node R) {
		Node L = R;
		if (L != null)
			for (; L.left != null; L = L.left)
				;
		return L;
	}
	//prints the (key,value) pairs belonging to the subtree rooted at R in order of increasing keys to file pointed to by out
	private String printInOrder(Node R) {
		String s = "";
		if (R != null) {
			printInOrder(R.left);
			s+= R.key + " " + R.value + "\n";
			s+= printInOrder(R.right) ;
		}
		return s;
	}
	//deletes all Nodes in the subtree rooted at N
	private void deleteAll(Node N) {
		if (N != null) {
			deleteAll(N.left);
			deleteAll(N.right);
		}
	}
	
	//--------------------------------------------

	// fields for Dictionary class
	private Node root;
	private int numPairs;

	public Dictionary() {
		root = null;
		numPairs = 0;
	}
	//returns true is Dictionary is empty
	@Override
	public boolean isEmpty() {
		if (numPairs == 0)
			return true;
		else
			return false;
	}
	//returns number of pairs in Dictionary
	@Override
	public int size() {
		return numPairs;
	}
	//returns the value such that (key,value) is in Dictionary or returns null if no such value exists
	@Override
	public String lookup(String key) {
		Node N = findKey(root, key);
		return (N == null ? null : N.value);
	}
	//inserts new (key, value) pair into Dictionary
	@Override
	public void insert(String key, String value) throws DuplicateKeyException {
		Node N, A, B;
		if (findKey(root, key) != null) {
			throw new DuplicateKeyException("cannot insert() duplicate key");
		}
		N = new Node(key, value);
		B = null;
		A = root;
		while (A != null) {
			B = A;
			if (key.compareTo(A.key) < 0)
				A = A.left;
			else
				A = A.right;
		}
		if (B == null)
			root = N;
		else if (key.compareTo(B.key) < 0)
			B.left = N;
		else
			B.right = N;
		numPairs++;
	}
	//deletes pair with the specified key
	@Override
	public void delete(String key) throws KeyNotFoundException {
		Node N, P, S;
		N = findKey(root, key);
		if (N == null) {
			throw new KeyNotFoundException("cannot delete() non-existent key");
		}
		if (N.left == null && N.right == null) { // case 1: no children
			if (N == root)
				root = null;
			else {
				P = findParent(N, root);
				if (P.right == N)
					P.right = null;
				else
					P.left = null;
			}
		} else if (N.right == null) { // case 2: left but no right children
			if (N == root)
				root = N.left;
			else {
				P = findParent(N, root);
				if (P.right == N)
					P.right = N.left;
				else
					P.left = N.left;
			}
		} else if (N.left == null) { // case 3: right but no left child
			if (N == root)
				root = N.right;
			else {
				P = findParent(N, root);
				if (P.right == N)
					P.right = N.right;
				else
					P.left = N.right;
			}
		} else { //case 4: two children: N.left!=null and N.right!=null
			S = findLeftMost(N.right);
			N.key = S.key;
			N.value = S.value;
			P = findParent(S, N);
			if(P.right ==S)
				P.right = S.right;
			else
				P.left = S.right;
		}
		numPairs--;
	}
	//resets Dictionary to the empty state
	@Override
	public void makeEmpty() {
		deleteAll(root);
		root = null;
		numPairs = 0;

	}
	//prints String representation of Dictionary
	@Override
	public String toString() {
		String s = printInOrder(root);
		return s;
	}

}
