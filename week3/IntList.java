public class IntList {
	public int first;
	public IntList rest;

	public IntList(int f, IntList r) {
		first = f;
		rest = r;
	}

	/** Return the size of the list using... recursion! */
	public int size() {
		if (rest == null) {
			return 1;
		}
		return 1 + this.rest.size();
	}

	/** Return the size of the list using no recursion! */
	public int iterativeSize() {
		IntList p = this;
		int totalSize = 0;
		while (p != null) {
			totalSize += 1;
			p = p.rest;
		}
		return totalSize;
	}

	/** Returns the ith item of this IntList. */
	public int get(int i) {
		if (i == 0) {
			return first;
		}
		return rest.get(i - 1);
	}

	public static IntList list(int... args){
		if(args.length == 0){
			return null;
		}

		IntList sentinel = new IntList(args[0], null);
		IntList ptr = sentinel;
		for (int i = 1; i < args.length; i++) {
			ptr.rest = new IntList(args[i], null);
			ptr = ptr.rest;
		}
		return sentinel;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < this.size(); i++) {
			sb.append(this.get(i));
			if (i != this.size() -1){
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public void skippify(){
		IntList p = this;
		int n = 1;
		while(p != null){
			IntList next = p.rest;
			for(int i = 0; i < n; i++){
				if(next == null){
					break;
				}
				next = next.rest;
			}
			p.rest = next;
			p = p.rest;
			n++;
		}
	}

	public static void removeDuplicates(IntList p){
		if (p == null){
			return;
		}

		IntList current = p.rest;
		IntList previous = p;
		while(current != null){
			if(previous.first != current.first){
				previous.rest = current;
				previous = current;
			}
			current = current.rest;
		}
	}





	public static void main(String[] args) {
		// IntList L = new IntList(15, null);
		// L = new IntList(10, L);
		// L = new IntList(5, L);

		// System.out.println(L.get(100));
		// IntList A = IntList.list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		// A.skippify();
		// System.out.println(A);

		IntList A = IntList.list(1, 2, 2, 3, 4, 5);
		removeDuplicates(A);
		System.out.println(A);
	}
} 