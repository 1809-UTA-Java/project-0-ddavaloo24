package com.revature.bank;
   
import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.revature.bank.accounts.*;
import com.revature.bank.dbs.FileIO;
import com.revature.bank.people.*;
import com.revature.bank.screens.BankAdminScreens;
import com.revature.bank.screens.CustomerScreens;
import com.revature.bank.screens.EmployeeScreens;
import com.revature.bank.logging.*;


public class Bank {
    public static void main(String[] args) {

        //Initial initializations of login object and scanner to check for answers
        Scanner sc = new Scanner(System.in);
        Login login = new Login();

        //Check which kind of account type to access
        Customer currentCustomer = null;
        Employee currentEmployee = null;
        BankAdmin currentBankAdmin = null;

        //Used to check if the correct input was given when asking for different inputs on the screens
        boolean check;
        String answer;
        int type;

        //Creates a default login screen and makes sure user enters yes or no
        do {
            System.out.print("Welcome to the Bank of Darius! Do you have an account with us? Yes or No \n" +
                    "Choice: ");
            answer = sc.nextLine().toLowerCase().trim();

            if(answer.equals("no") || answer.equals("yes")) break;
            else {
                System.out.println("Sorry! I didn't get that! Please try again\n");
            } 
        } while(true);

        do {
            if(answer.equals("no")) {
                //If the user does not have an account, make one for them
                login.createAccount();
                check = false;
            }
            else if(answer.equals("yes")) {
                //If the user already has an account
                check = false;
            }
            else {
                //If the user enters anything other than a yes or no
                System.out.println("Please write yes or no");
                answer = sc.nextLine().toLowerCase();
                check = true;   
            }
        } while(check);

        do {
            do {
                System.out.print("\nWhat kind of account would you like to login to? \n" +
                                "1. Customer \n" +
                                "2. Employee \n" +
                                "3. Admin \n" + 
                                "Choice: ");
                try{
                    try{
                        type = Integer.parseInt(sc.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.println("\nYour chosen number must be either 1, 2, or 3");
                        continue;
                    }

                    if(type < 4 && type > 0) break;
                    else {
                        System.out.println("Your chosen number must be either 1, 2, or 3");
                    }
                } catch(InputMismatchException e) {
                    System.out.println("You must choose either 1, 2, or 3 as your options");
                    sc.nextLine();
                }
            } while( true );

            //Login the right user based on which database they are in
            switch(type) {
                case 1:
                    currentCustomer = login.loginCustomer();
                break;
                case 2:
                    currentEmployee = login.loginEmployee();
                break;
                case 3:
                    currentBankAdmin = login.loginBankAdmin();
                break;
            }

            if(currentCustomer == null && currentEmployee == null && currentBankAdmin == null) {
                System.out.println("Your credentials were not in the chosen database. \n");
            }
        } while(currentCustomer == null && currentEmployee == null && currentBankAdmin == null);
        

        if(type == 1) {
            //IF USER IS A CUSTOMER
            CustomerScreens.CustomerMainMenu(currentCustomer);
        }
        else if(type == 2) {
            //IF USER IS AN EMPLOYEE
            EmployeeScreens.EmployeeMainMenu(currentEmployee);
        }
        else {
            //IF USER IS AN ADMIN
            BankAdminScreens.BankAdminMainMenu(currentBankAdmin);
        }

        
        











        














        sc.close();

    }
}