package Practice.NestedInterfaces;

public class Main {
    public static void main(String[] args) {
        ClassA.NestedInterface nested = new ClassB();
        nested.isNegative(-10);
    }
}
