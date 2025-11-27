package com.myshop;

import com.myshop.app.ApplicationContext;
import com.myshop.ui.MainMenu;
import org.mindrot.jbcrypt.BCrypt;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = ApplicationContext.getInstance();
        MainMenu menu = new MainMenu();
        System.out.println(org.mindrot.jbcrypt.BCrypt.hashpw("admin123", org.mindrot.jbcrypt.BCrypt.gensalt()));
        menu.start();
    }
}
