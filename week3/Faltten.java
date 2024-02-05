import java.util.Arrays;

public class Faltten {
    public static void main(String[] args) {
        int[][] x = {{1, 2, 3}, {}, {7, 8}};
        int[] flatten = flatten(x);
        System.out.println(Arrays.toString(flatten));
    }

    /**
     * takes in a 2-D array x and returns a 1-D array that
     * contains all of the arrays in x concatenated together.
     * For example, flatten({{1, 2, 3}, {}, {7, 8}}) should return {1, 2, 3, 7, 8}.
     */
    public static int[] flatten(int[][] x){
        int totalLength = 0;

        for(int i = 0; i < x.length; i++){
            totalLength += x[i].length;
        }

        int[] a = new int[totalLength];
        int aIndex = 0;
        for(int[] subarray : x){
            System.arraycopy(subarray, 0, a, aIndex, subarray.length);
            aIndex += subarray.length;
        }

        return a;
    }


}
