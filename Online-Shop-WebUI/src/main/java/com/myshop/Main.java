package com.myshop;

import com.myshop.app.ApplicationContext;
import com.myshop.ui.MainMenu;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = ApplicationContext.getInstance();
        System.out.println("Welcome to MyShop Console App");
        new MainMenu().start();
        ctx.getScanner().close();
        System.out.println("Application stopped.");
    }
}
