package com.revature.bank;
   
import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.revature.bank.accounts.*;
import com.revature.bank.people.*;
import com.revature.bank.logging.*;


public class Bank {
    public static void main(String[] args) {

        Scanner sc = new Scanner( System.in );
        Login login = new Login();
        User currentUser = null;
        int type;

        //Used to check if the correct input was given when asking for a yes or no on the main screen
        boolean check;

        //Creates a default login screen
        System.out.println("Welcome to the Bank of Darius! Do you have an account with us?");
        String answer = sc.nextLine().toLowerCase();

        do {

            System.out.println("What kind of account would you like to use? \n" +
                                "1. Customer \n" +
                                "2. Employee \n" +
                                "3. Admin");

        
            try{
                type = sc.nextInt();

                if(type < 4 && type > 0) {
                    break;
                }
                else {
                    System.out.println("Your chosen number must be either 1, 2, or 3");
                }

            } catch(InputMismatchException e) {
                System.out.println("You must choose either 1, 2, or 3 as your options");
                sc.nextLine();
            }

        } while( true );

        do {
            //If the user does not have an accout, make one for them
            if(answer.equals("no")) {

                //Login using the given username and password by checking from the hashmaps
                login.createAccount(type);

                //IF A CUSTOMER
                if( type == 1 ) {
                    currentUser = login.loginCustomer();
                    check = false;
                }
                //IF AN EMPLOYEE
                else if ( type == 2 ) {
                    currentUser = login.loginEmployee();
                    check = false;
                }
                //IF AN ADMIN
                else {
                    currentUser = login.loginBankAdmin();
                    check = false;
                }
            }
            else if(answer.equals("yes")) {
                //If the user already has an account
                //Login using the given username and password by checking from the hashmaps
                //IF A CUSTOMER
                if( type == 1 ) {
                    currentUser = login.loginCustomer();
                    check = false;
                }
                //IF AN EMPLOYEE
                else if ( type == 2 ) {
                    currentUser = login.loginEmployee();
                    check = false;
                }
                //IF AN ADMIN
                else {
                    currentUser = login.loginBankAdmin();
                    check = false;
                }
            }
            else {
                //If the user enters anything other than a yes or no
                System.out.println("Please write yes or no");
                answer = sc.nextLine().toLowerCase();
                check = true;   
            }
        } while(check);


        //IF USER IS A CUSTOMER
        System.out.println( "Login successful! Hello " + currentUser.toString());
        System.out.println( "Would you like to: \n" +
                            "1. Display current bank accounts \n" +
                            "2. Apply for an account" );
        int opt = sc.nextInt();

        //To display all current open accounts
        if(opt == 1) {
            currentUser.displayMyAccs();
        }
        //To apply for a joint or single account
        else if(opt == 2) {
            System.out.println("Would you like to open a single or joint account?");
            //if single account then currentUser.applyForAcc();
            //else joint account then currentUser.applyForAcc() and with a given name also name.applyForAcc();
        }
        else {

        }


        //IF USER IS AN EMPLOYEE
        //IF USER IS AN ADMIN











        














        sc.close();

    }
}