package Practice.MultipleInterfaces;

public interface Morning {
    default void greetings(){
        System.out.println("Good Morning");
    };
}
