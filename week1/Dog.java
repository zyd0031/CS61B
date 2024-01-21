package week1;

public class Dog {
    public int weightInPounds; // instance variable
    public static String bioname = "Canis familaris";

    /** One integer constructor for dogs. */
    public Dog(int w){
        weightInPounds = w;
    }

    /** instance method */
    public void makeNoise(){
        if (weightInPounds < 10){
            System.out.println("yip!");
        }else if (weightInPounds < 30){
            System.out.println("bark!");
        }else {
            System.out.println("woooof!");
        }
    }

    public static Dog maxDog(Dog d1, Dog d2){
        if (d1.weightInPounds > d2.weightInPounds){
            return d1;
        }
        return d2;

    }

    public Dog maxDog(Dog d2){
        if (this.weightInPounds > d2.weightInPounds){
            return this;
        }
        return d2;
    }
}
