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
        boolean exit = true;

        System.out.println("Welcome to the Bank of Darius!");


        do{
            exit = LoginScreen.loginMainMenu(sc);

        } while(exit);

        System.out.println("\nHave a good day!");
        

        sc.close();
    }
}