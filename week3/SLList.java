/**
 * SLList
 */
public class SLList {

    private class IntNode{
        public int item;
        public IntNode next;
        public IntNode(int item, IntNode next){
            this.item = item;
            this.next = next;
        }
    }

    private IntNode first;

    public void addFirst(int x){
        first = new IntNode(x, first);
    }

    public void insert(int item, int position){
        if (position < 0){
            throw new IllegalArgumentException("Position connot be negative");
        }

        if (position == 0){
            addFirst(item);
        }

        IntNode ptr = first;
        int pos = 0;
        IntNode add = new IntNode(item, null);

        while(ptr.next != null){
            if(++pos == position){
                add.next = ptr.next;
                ptr.next = add;
            }
            ptr = ptr.next;
        }
        if(position != 0){
            ptr.next = add;
        }
    }

    public void reverse(){
        IntNode front = null;
        IntNode ptr = first;
        while(ptr != null){
            IntNode reminderOfFront = ptr;
            reminderOfFront.next = front;
            front = ptr;
            ptr = ptr.next;
        }
        first = front;
    }

    


}