package com.revature.bank.logging;

import java.io.Console;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.io.*;

import com.revature.bank.accounts.BankAccount;
import com.revature.bank.people.*;
import com.revature.bank.util.ConnectionUtil;
import com.revature.bank.util.SuperDao;
import com.revature.bank.dbs.FileIO;

/**
 * 
 * A helper class used to login the user depending of which type of account
 * they want to access. Also uses the LoginValidator to provide further checks
 */
public class Login {

    //Used to store the account loaded up once a user has logged in
    private Customer currentCustomer = null;
    private Employee currentEmployee = null;
    private BankAdmin currentBankAdmin = null;

    //Holder variables
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    //Used to create an account for the user depending of which kind they want
    public void createAccount(Scanner sc) {

        //Used for checking account creation checks
        boolean checker;
        int type;

        do {
            System.out.print("What kind of account would you like to make? \n" +
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
                else System.out.println("\nYour chosen number must be either 1, 2, or 3");

            } catch(InputMismatchException e) {
                System.out.println("\nYou must choose either 1, 2, or 3 as your options");
                sc.nextLine();
            }
        } while( true );

        //Ask the user for account information
        System.out.println("\nPlease fill out the following to create an account:");

        do {
            //First name request and check
            System.out.print("First name must be 2-20 characters with no numbers or special characters \n" +        
                    "First name : ");
            firstName = sc.nextLine();
            checker = LoginValidator.nameChecker(firstName);
        } while(checker);
        checker = true;

        do {
            //Last name request and check
            System.out.print("\nLast name must be 2-20 characters with no numbers or special characters \n" +
                    "Last name: ");
            lastName = sc.nextLine().trim();
            checker = LoginValidator.nameChecker(lastName);
        } while(checker);
        checker = true;

        do {
            //Username request and check
            System.out.print("\nUsername must be 6-12 characters and cannot include underscores or spaces \n" +
                        "Username : ");
            username = sc.nextLine();
            checker = LoginValidator.usernameChecker(username);
        } while(checker);
        checker = true;

        do{
            //Password request and check
            System.out.print("\nPassword must be 8-12 characters and cannot include underscores or spaces \n" +
                        "Password: ");
            password = sc.nextLine();
            checker = LoginValidator.passwordChecker(password);
        } while(checker);

        //Create the account and persist it in a file
        switch(type) {
            case 1:
            //Create a new customer object and insert their fields in the dbs
            Customer c = new Customer(username, password, firstName, lastName);            
            SuperDao.insertCustomer(c.getCustomerID(), username, password, firstName, lastName);
            break;

            case 2:
            //Create a new employee object and insert their fields in the dbs
            Employee e = new Employee(username, password, firstName, lastName);
            SuperDao.insertEmployee(e.getEmployeeID(), username, password, firstName, lastName);
            break;

            case 3:
            //Create a new admin object and insert their fields in the dbs
            BankAdmin b = new BankAdmin(username, password, firstName, lastName);
            SuperDao.insertAdmin(b.getBankAdminID(), username, password, firstName, lastName);
            break;

            default:
            System.out.println("Invalid person");
        }

        System.out.println("Account created! Congrats!");
    }

    //Login function specifically for the customer
    public Customer loginCustomer(Scanner sc) {

        boolean lookupStatus;

        System.out.println("\nPlease enter you username and password");
        System.out.print("Username: ");
        username = sc.nextLine();
        System.out.print("Password: ");
        password = sc.nextLine();

        do {
            lookupStatus = SuperDao.checkLogin(username, password);
            if(lookupStatus) {
                currentCustomer = SuperDao.retrieveCustomerAccount(username, password);
                currentCustomer.loadAccounts();
                return currentCustomer;
            }
            else {
                System.out.println("Your username or password was incorrect. Please try again.");
                return null;
            }
        } while(currentCustomer == null);
    }

    public Employee loginEmployee(Scanner sc) {

        boolean lookupStatus;

        System.out.println("\nPlease enter you username and password");
        System.out.print("Username: ");
        username = sc.nextLine();
        System.out.print("Password: ");
        password = sc.nextLine();

        do {
            lookupStatus = SuperDao.checkLogin(username, password);
            if(lookupStatus) {
                currentEmployee = SuperDao.retrieveEmployeeAccount(username, password);
                currentEmployee.loadAccounts();
                return currentEmployee;
            }
            else {
                System.out.println("Your username or password was incorrect. Please try again.");
                return null;
            }
        } while(currentEmployee == null);

    }

    public BankAdmin loginBankAdmin(Scanner sc) {

        boolean lookupStatus;

        System.out.println("\nPlease enter you username and password");
        System.out.print("Username: ");
        username = sc.nextLine();
        System.out.print("Password: ");
        password = sc.nextLine();

        do {
            lookupStatus = SuperDao.checkLogin(username, password);
            if(lookupStatus) {
                currentBankAdmin = SuperDao.retrieveAdminAccount(username, password);
                currentBankAdmin.loadAccounts();
                return currentBankAdmin;
            }
            else {
                System.out.println("Your username or password was incorrect. Please try again.");
                return null;
            }
        } while(currentBankAdmin == null);

    }
}