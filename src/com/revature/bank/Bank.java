package com.revature.bank;
   
import java.util.Scanner;
import com.revature.bank.screens.LoginScreen;

/**
 * Main class used to run the entire banking app
 */
public class Bank {
    public static void main(String[] args) {

        //Initial initializations of login object and scanner to check for answers
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to the Bank of Darius!");

        LoginScreen.loginMainMenu(sc);

        sc.close();
    }
}