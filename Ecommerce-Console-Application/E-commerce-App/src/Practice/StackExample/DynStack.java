package Practice.StackExample;

public class DynStack implements IntStack{
    int[] stack;
    int top;

    DynStack(int size){
        stack = new int[size];
        top = -1;
    }

    public void push(int item){
        if(top == stack.length -1){
            System.out.println("Stack Overflow! Increasing the size");
            int[] temp = new int[stack.length * 2];
            System.arraycopy(stack, 0, temp, 0, stack.length);
            stack = temp;
        }
        stack[++top] = item;
    }

    public int pop(){
        if(top < 0){
            System.out.println("Stack Underflow");
            return -1;
        }
        else {
            return stack[top--];
        }
    }

}
