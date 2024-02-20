public class D {
    public static void main(String[] args) {
        A c0 = new C();
        // c0.m2();
        A a1 = (A) c0;
        C c2 = (C) a1;

        // c2.m3();
        // c2.m4();
        // c2.m5();

        // ((C)c0).m3();
        // (C)c0.m3();

    }
}
