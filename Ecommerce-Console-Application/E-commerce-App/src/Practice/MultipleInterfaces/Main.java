package Practice.MultipleInterfaces;

public class Main implements Morning, Evening{
//    @Override
//    public void greetings(){
//
//    }
//    @Override
    public void greetings(){
        System.out.println("");
//        Evening.super.greetings();
        Morning.super.greetings();
    }
}
