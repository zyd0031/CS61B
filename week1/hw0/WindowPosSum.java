package week1.hw0;

public class WindowPosSum {
    public static void main(String[] args) {
        int[] a = {1, 2, -3, 4, 5, 4};
        int n = 3;
        windowPosSum(a, n);
        System.out.println(java.util.Arrays.toString(a));
    }

    /** 
     * replaces each element a[i] with the sum of a[i] through a[i + n], but only if a[i] is positive valued. 
     * If there are not enough values because we reach the end of the array, we sum only as many values as we have.
     * 
     */
    public static void windowPosSum(int[] a, int n){
        for (int i = 0; i < a.length; i++) {
            if(a[i] < 0){
                continue;
            }
            int last_index = (i + n < a.length) ? (i + n) : a.length - 1;
            int replace = 0;
            for (int j = i; j <= last_index; j++) {
                replace += a[j];
            }
            a[i] = replace;
        }
    }
}
