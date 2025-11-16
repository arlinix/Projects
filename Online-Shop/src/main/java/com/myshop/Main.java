package com.myshop;

import com.myshop.app.ApplicationContext;
import com.myshop.ui.MainMenu;

public class Main {
    public static void main(String[] args) {
        // initialize context (this loads shop & services)
        ApplicationContext ctx = ApplicationContext.getInstance();
        System.out.println("Welcome to MyShop Console App");
        MainMenu menu = new MainMenu();
        menu.start();
        // finalize
        ctx.getScanner().close();
        System.out.println("Application stopped.");
    }
}
