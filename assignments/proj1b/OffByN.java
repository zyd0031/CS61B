public class OffByN implements CharacterComparator{
    private int N;
    public OffByN(int N){
        this.N = N;
    }
    public boolean equalChars(char x, char y){
        return Math.abs(x - y) == N;
    }

    public static void main(String[] args) {
        OffByN offBy5 = new OffByN(5);
        System.out.println(offBy5.equalChars('a', 'f'));
    }
    
}

