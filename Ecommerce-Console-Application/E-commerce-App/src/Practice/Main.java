package Practice;

public class Main {
    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.printName("My Name");
        demo.printInfo2();

        Practice practice;
        practice = demo;
        practice.printInfo2();
        practice.printName("Interface");


    }
}
