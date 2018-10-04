package com.revature.bank.logging;

import java.io.Console;
import java.io.FileReader;
import java.util.*;
import java.io.*;

import com.revature.bank.accounts.*;
import com.revature.bank.people.*;
import com.revature.bank.dbs.*;

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

    //Pathway to which all login files are stored
    private String pathway = "/home/developer/Workspace/project-0-ddavaloo24/src/com/revature/bank/dbs/";

    //Holder variables
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private int type;

    //Used for checking account creation checks
    private boolean checker;

    //Used to create an account for the user depending of which kind they want
    public void createAccount(Scanner sc) {

        do {
            System.out.print("\nWhat kind of account would you like to make? \n" +
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


        //Write to the Logins.txt file the login information just created in a one line format
        String pattern = username + "_" + password + "_" + firstName + "_" + lastName + "\n";

        //Write the pattern of personal information to a central database of user accounts
        FileIO.write("AllUserAccounts", pattern);

        //Create the account and persist it in a file
        switch(type) {
            case 1:
            //Create a new customer and add to a doc that holds the username and customerID
            //Also create a file that is titled the customerID and holds the customer object
            Customer c = new Customer(username, password, firstName, lastName);
            pattern = username + "\n" + c.getCustomerID() + "\n";
            FileIO.write("AllCustomers", pattern);
            FileIO.serialize(c.getCustomerID(), c);
            break;
            case 2:
            //Create a new employee and store it into a file titled its employeeID
            //Also add to file that holds all employee accounts using username and employeeID
            Employee e = new Employee(username, password, firstName, lastName);
            pattern = username + "\n" + e.getEmployeeID() + "\n";
            FileIO.write("AllEmployees", pattern);
            FileIO.serialize(e.getEmployeeID(), e);
            break;
            case 3:
            //Create a new admin and serialize it into a file using the AdminID
            //Add the admin username and ID to a database for admin
            BankAdmin b = new BankAdmin(username, password, firstName, lastName);
            pattern = username + "\n" + b.getBankAdminID() + "\n";
            FileIO.write("AllAdmins", pattern);
            FileIO.serialize(b.getBankAdminID(), b);
            break;
            default:
            System.out.println("Invalid person");
        }

        System.out.println("Account created! Congrats!");
    }

    //Login function specifically for the customer
    public Customer loginCustomer(Scanner sc) {

        boolean lookupStatus;
        String accessor;
        String fileName;
        String fullFile = pathway + "AllCustomers";
        File file = new File(fullFile);

        do {
            System.out.println("\nPlease enter you username and password");
            System.out.print("Username: ");
            username = sc.nextLine();
            System.out.print("Password: ");
            password = sc.nextLine();
            String pattern = username + "_" + password + "_";

            lookupStatus = FileIO.lookupLogin("AllUserAccounts", pattern);
            if(lookupStatus) {

                fileName = null;
                try(Scanner sc1 = new Scanner(file)) {
                    while(sc1.hasNextLine()) {

                        accessor = sc1.nextLine();
                        if(accessor.equals(username)) {
                            fileName = sc1.nextLine();
                            break;
                        }
                    }
                } catch(FileNotFoundException e) {
                    System.out.println("There is no customer account with those credentials");
                    continue;
                }

                if(fileName == null) return null;

                currentCustomer = FileIO.deSerialize(fileName, Customer.class);
                currentCustomer.loadAccounts();
                return currentCustomer;
            }
            else {
                System.out.println("Your username or password was incorrect. Please try again.");
                return null;
            }
        } while(currentCustomer == null);

        return null;
    }

    public Employee loginEmployee(Scanner sc) {

        boolean lookupStatus;
        String accessor;
        String fileName;
        String fullFile = pathway + "AllEmployees";
        File file = new File(fullFile);

        do {
            System.out.println("\nPlease enter you username and password");
            System.out.print("Username: ");
            username = sc.nextLine();
            System.out.print("Password: ");
            password = sc.nextLine();
            String pattern = username + "_" + password + "_";

            lookupStatus = FileIO.lookupLogin("AllUserAccounts", pattern);
            if(lookupStatus) {

                fileName = null;
                try(Scanner sc1 = new Scanner(file)) {
                    while(sc1.hasNextLine()) {

                        accessor = sc1.nextLine();
                        if(accessor.equals(username)) {

                            fileName = sc1.nextLine();
                            break;
                        }
                    }
                } catch(FileNotFoundException e) {
                    System.out.println("There is no employee account with those credentials");
                    continue;
                }

                if(fileName == null) return null;

                currentEmployee = FileIO.deSerialize(fileName, Employee.class);
                currentEmployee.loadAccounts();
                return currentEmployee;
            }
            else {
                System.out.println("Your username or password was incorrect. Please try again.");
                return null;
            }
        } while(currentEmployee == null);

        return null;
    }

    public BankAdmin loginBankAdmin(Scanner sc) {

        boolean lookupStatus;
        String accessor;
        String fileName;
        String fullFile = pathway + "AllAdmins";
        File file = new File(fullFile);

        do {
            System.out.println("\nPlease enter you username and password");
            System.out.print("Username: ");
            username = sc.nextLine();
            System.out.print("Password: ");
            password = sc.nextLine();
            String pattern = username + "_" + password + "_";

            lookupStatus = FileIO.lookupLogin("AllUserAccounts", pattern);
            if(lookupStatus) {

                fileName = null;
                try(Scanner sc1 = new Scanner(file)) {
                    while(sc1.hasNextLine()) {

                        accessor = sc1.nextLine();
                        if(accessor.equals(username)) {
                            fileName = sc1.nextLine();
                            break;
                        }
                    }
                } catch(FileNotFoundException e) {
                    System.out.println("There is no admin account with those credentials");
                    continue;
                }

                if(fileName == null) return null;

                currentBankAdmin = FileIO.deSerialize(fileName, BankAdmin.class);
                currentBankAdmin.loadAccounts();
                return currentBankAdmin;
            }
            else {
                System.out.println("Your username or password was incorrect. Please try again.");
                return null;
            }
        } while(currentBankAdmin == null);

        return null;
    }
}