package week1.hw0;

public class EnahncedForBreakDemo {
    public static void main(String[] args) {
        String[] a = {"cat", "dog", "laser horse", "ketchup", "horse", "horbse"};

        for (String s : a) {
            for (int j = 0; j < 3; j++) {
                System.out.println(s);
                if (s.contains("horse")){
                    break;
                }
            }
        }
    }
}
