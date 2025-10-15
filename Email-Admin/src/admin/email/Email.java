package admin.email;

import java.util.Random;
import java.util.Scanner;

public class Email {
    private String firstName;
    private String lastName;
    private String department;
    private String email;
    private String password;
    private int mailBoxCapacity = 500;
    private String alternateEmail;
    private String companySuffix = "arinixtech.com";

    public Email(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = setDepartment();
        this.password = PasswordGenerator.generatePassword(12);
        this.email = firstName.toLowerCase() + "." + lastName.toLowerCase()+ "@" + department + "."+ companySuffix;

    }

    //Ask for development
    private String setDepartment(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter: \n1: For Sales \n2: For Development\n3: For Accounting\n 0 for none");
        int choice = sc.nextInt();
        switch(choice){
            case 1 -> {
                return "sls";
            }
            case 2 -> {
                return "dev";
            }
            case 3 -> {
                return "acc";
            }
            default ->
            {return "";}
        }
    }

    public void setEmailCapacity(int capacity){
        this.mailBoxCapacity = capacity;
    }

    public void setAlternateEmail(String alternateEmail){
        this.alternateEmail = alternateEmail;
    }

    public void getEmail() {
        System.out.println("Email: " + email);;
    }

    public void getMailBoxCapacity() {
        System.out.println("Mail Box Capacity: " + mailBoxCapacity);
    }

    public void getAlternateEmail() {
        if(alternateEmail == null){
            System.out.println("No Alternate mail");
        }else{
            System.out.println("Alternate Email: " + alternateEmail);
        }
    }

    public void getDepartment() {
        System.out.println("Department: " + department);
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void setMailBoxCapacity(int capacity){
        mailBoxCapacity = capacity;
    }

    public void showInfo(){
        System.out.println("First Name: " + firstName + "\n" + "Last Name: " + lastName + "\n"
        + "Email: " + email);
    }


}
