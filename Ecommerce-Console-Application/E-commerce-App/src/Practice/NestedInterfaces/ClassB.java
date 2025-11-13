package Practice.NestedInterfaces;

public class ClassB implements ClassA.NestedInterface{
    public void isNegative(int num){
        if(num < 0){
            System.out.println("Is Negative");
        }
    }
}
