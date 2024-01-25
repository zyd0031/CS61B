public class IntList {
	public int first;
	public IntList rest;

	public IntList(int f, IntList r) {
		first = f;
		rest = r;
	}

	/** Return the size of the list using... recursion! */
	public static int size(IntList L) {
		if (L == null){
			return 0;
		}
		if (L.rest == null){
			return 1;
		}
		return 1 + IntList.size(L.rest);
	}

	/** Return the size of the list using no recursion! */
	public int iterativeSize() {
		int totalSize = 0;
		IntList p = this;
		while (p.rest != null){
			++totalSize;
			p = p.rest;

		}
		return ++totalSize;
	}

	/** Returns the ith value in this list.*/
	public int get(int i) {
		if (i == 0){
			return this.first;
		}
		return this.rest.get(i - 1);
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		IntList p = this;
		while(p != null){
			sb.append(p.first);
			if (p.rest != null){
				sb.append(", ");
			}
			p = p.rest;
		}
		sb.append("]");
		return sb.toString();
	}
	/**
	 * Implement square and squareMutative which are static methods that both take in
	an IntList L and return an IntList with its integer values all squared. square does
	this non-mutatively with recursion by creating new IntLists while squareMutative
	uses a recursive approach to change the instance variables of the input IntList L.
	 */
	public static int mathsquare(int number){
		return number * number;
	}

	public static IntList square(IntList L){
		if (L == null){
			return null;
		}
		IntList Ltemp = L;
		IntList Q = new IntList(mathsquare(Ltemp.first), square(Ltemp.rest));
		return Q;

	}

	public static IntList squareMutative(IntList L){
		if (L == null){
			return null;
		}
		L.first = mathsquare(L.first);
		squareMutative(L.rest);
		return L;
	}


	public static void main(String[] args) {
		IntList L = new IntList(15, null);
		L = new IntList(10, L);
		L = new IntList(5, L);
		// IntList L = null;

		// System.out.println(IntList.size(L));
		// System.out.println(L.iterativeSize());
		// System.out.println(L);
		// System.out.println(IntList.square(L));
		System.out.println(IntList.squareMutative(L));
		System.out.println(L);

		// System.out.println(L.iterativeSize());
	}
} 