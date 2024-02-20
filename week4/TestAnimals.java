public class TestAnimals {
    public static void main(String[] args) {
        Animal a = new Animal("Pluto", 10);
        Cat c = new Cat("Garfield", 6);
        Dog d = new Dog("Fido", 4);

        a.greet();
        c.greet();
        d.greet();
        a = c;
        // if you attempt to call a method that exists only in the Cat class (and not in the Animal class), 
        // you need to explicitly cast a back to the Cat type. 
        ((Cat)a).greet(); 
        a.greet();

        a = new Dog("Spot", 10);
        d = (Dog)a;
    }
}
