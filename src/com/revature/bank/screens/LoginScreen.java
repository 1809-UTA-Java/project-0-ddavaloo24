package com.revature.bank.screens;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.revature.bank.accounts.*;
import com.revature.bank.dbs.FileIO;
import com.revature.bank.people.*;
import com.revature.bank.logging.*;

public class LoginScreen {

    public static boolean loginMainMenu(Scanner sc) {

        Login login = new Login();

        //Check which kind of account type to access
        Customer currentCustomer = null;
        Employee currentEmployee = null;
        BankAdmin currentBankAdmin = null;

        //Used to check if the correct input was given when asking for different inputs on the screens
        String answer;
        int type;

        do {
            //Creates a default login screen and makes sure user enters yes or no
            do {           
                System.out.println("\n----------------------------------------------------");
                System.out.print("Would you like to login or signup or exit? \n" +
                        "Choice: ");
                answer = sc.nextLine().toLowerCase().trim();

                if(answer.equals("login") || answer.equals("signup")) break;
                else if(answer.equals("exit")) return false;
                else System.out.println("Sorry! I didn't get that! Please try again\n");

            } while(true);

            if(answer.equals("signup")) {
                //If the user does not have an account, make one for them
                login.createAccount(sc);
            }
            if(answer.equals("login")) {
                //If the user already has an account
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
                            else System.out.println("Your chosen number must be either 1, 2, or 3");
                        } catch(InputMismatchException e) {
                            System.out.println("You must choose either 1, 2, or 3 as your options");
                            sc.nextLine();
                        }
                    } while( true );
        
                    //Login the right user based on which database they are in
                    switch(type) {
                        case 1:
                            currentCustomer = login.loginCustomer(sc);
                        break;
                        case 2:
                            currentEmployee = login.loginEmployee(sc);
                        break;
                        case 3:
                            currentBankAdmin = login.loginBankAdmin(sc);
                        break;
                    }
        
                    if(currentCustomer == null && currentEmployee == null && currentBankAdmin == null) {
                        System.out.println("Your credentials were not in the chosen database. \n");
                    }
                } while(currentCustomer == null && currentEmployee == null && currentBankAdmin == null);
                break;
            }
        } while(true);

        //Log the user and give them the appropriate main menu
        if(type == 1) return CustomerScreens.CustomerMainMenu(currentCustomer, sc);
        else if(type == 2) return EmployeeScreens.EmployeeMainMenu(currentEmployee, sc);
        else return BankAdminScreens.BankAdminMainMenu(currentBankAdmin, sc);
    }
    
}