# cs61B
course website: https://sp18.datastructur.es/
## WEEK1
```bash
$ javac -cp . week1/HelloWorld.java
$ java -cp . week1.HelloWorld
```
<br>

**javac** is very smart, run
```bash
$ javac -cp . week1/DogLauncher.java
```
it intelligently identifies `Dog.java` as a dependency of `DogLauncher.java` and automatically recompiles `Dog.java`.
## WEEK2
### Mystery of the Walrus
#### Assignment
**Reference Type:**<br>
```java
Walrus a = new Walrus(1000, 8.3);
Walrus b;
b = a;
b.weight = 5;
System.out.println(a);
System.out.println(b);
```
*weight: 5, tusk size: 8.30*<br>
*weight: 5, tusk size: 8.30*<br>
Change to b affect a.<br><br>
Explanation:<br>
```java
public static class Walrus {
	public int weight;
	public double tuskSize;
 
	public Walrus(int w, double ts) {
   	   weight = w;
   	   tuskSize = ts;
	}
}
```
When we **instantiate** an Object (e.g. Dog, Walrus, Planet):<br>
- Java first allocates a box of bits for each instance variable of the class and fills them with a default value (e.g. 0, null).
- The constructor then usually fills every such box with some other value.<br>

When we **declare** a variable of any reference type (Walrus, Dog, Planet):
- Java allocates exactly a box of size 64 bits, no matter what type of object.
- These bits can be either set to:
    * Null (all zeros).
        ```java
        Walrus someWalrus;
        someWalrus = null;
        ```
    * The 64 bit “*address*” of a specific instance of that class (returned by new).
        ```java
        Walrus someWalrus;
        someWalrus = new Walrus(1000, 8.3);
        ```

**Primitive Type:**<br>
(byte, short, int, long, float, double, boolean, char)
```java
int x = 5;
int y;
y = x;
x = 2;
System.out.println("x is: " + x);
System.out.println("y is: " + y);
```
*x is: 2*<br>
*y is: 5*<br>
Change to b does not change a. <br><br>
Explanation:<br>
y = x copies all the bits from x into y.<br><br>
![](/week2/mysteryofthewalrus/mystery.png)<br><br>
#### Parameter Passing
Passing parameters obeys the same rule: Simply copy the bits to the new scope.<br>
```java
public static double average(double a, double b) {
	return (a + b) / 2;
}
 
public static void main(String[] args) {
	double x = 5.5;
	double y = 10.5;
	double avg = average(x, y);
}
```
![](/week2/mysteryofthewalrus/parameterpassing.png)