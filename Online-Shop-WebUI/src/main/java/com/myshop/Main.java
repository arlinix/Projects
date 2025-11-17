package com.myshop;

import com.myshop.app.ApplicationContext;
import com.myshop.ui.MainMenu;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = ApplicationContext.getInstance();
        MainMenu menu = new MainMenu();
        menu.start();
    }
}
