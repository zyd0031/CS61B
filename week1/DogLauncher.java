package week1;

public class DogLauncher {
    public static void main(String[] args) {
        Dog d = new Dog(25);
        Dog d2 = new Dog(100);
        
        // Dog bigger = Dog.maxDog(d, d2);
        Dog bigger = d.maxDog(d2);
        bigger.makeNoise();
        // System.out.println(d.bioname);
        System.out.println(Dog.bioname);
        // d.makeNoise();
    }
}
