package Practice.StackExample;

public class InterfaceTest {
    public static void main(String[] args) {
        FixedStack stack1 = new FixedStack(5);
        FixedStack stack2 = new FixedStack(10);

        stack2.push(10);
        stack2.push(20);
        stack1.push(-10);
        stack1.push(-20);

        System.out.println(stack1.pop());
        System.out.println(stack2.pop());

        DynStack stack3 = new DynStack(5);
//        stack3.push(10);
//        stack3.push(10);
//        stack3.push(10);
//        stack3.push(10);
//        stack3.push(10);
//        stack3.push(10);
        System.out.println(stack3.pop());

     }
}
