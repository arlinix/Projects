package Practice;

public interface Practice {
    String name = "Aman";

    public void printName(String name);
    public void otherMethod();

    public static void printName(){
        System.out.println("Interface Method");
    }

    public static void printInfo(){
        System.out.println("Executing Static Block");
    }

    private void printInfo1(){
        System.out.println("Private Method");
    }

    default void printInfo2(){
        printInfo();
        printInfo1();
        System.out.println("Default Method");
    }

}
