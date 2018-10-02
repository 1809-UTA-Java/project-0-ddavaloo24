package com.revature.bank.logging;

import java.io.Console;
import java.io.FileReader;
import java.util.*;
import java.io.*;
import com.revature.bank.accounts.*;
import com.revature.bank.people.*;
import com.revature.bank.dbs.*;


public class Login {

    protected Customer currentCustomer = null;
    protected Employee currentEmployee = null;
    protected BankAdmin currentBankAdmin = null;

    private String pathway = "/home/developer/Workspace/project-0-ddavaloo24/src/com/revature/bank/dbs/";

    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected int type;

    //Used for checking account creation checks
    protected boolean checker;

    public void createAccount() {
        Scanner sc = new Scanner( System.in );

        do {
            System.out.println("What kind of account would you like to make? \n" +
                                "1. Customer \n" +
                                "2. Employee \n" +
                                "3. Admin");

            try{
                try{
                    type = Integer.parseInt(sc.nextLine());
                } catch(NumberFormatException e) {
                    System.out.println("\nYour chosen number must be either 1, 2, or 3");
                    continue;
                }

                if(type < 4 && type > 0) {
                    break;
                }
                else {
                    System.out.println("\nYour chosen number must be either 1, 2, or 3");
                }
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
            System.out.print("Last name must be 2-20 characters with no numbers or special characters \n" +
                    "Last name: ");
            lastName = sc.nextLine().trim();
            checker = LoginValidator.nameChecker(lastName);
        } while(checker);
        checker = true;

        do {
            //Username request and check
            System.out.print("Username must be 6-12 characters and cannot include underscores or spaces \n" +
                        "Username : ");
            username = sc.nextLine();
            checker = LoginValidator.usernameChecker(username);
        } while(checker);
        checker = true;

        do{
            //Password request and check
            System.out.print("Password must be 8-12 characters and cannot include underscores or spaces \n" +
                        "Password: ");
            password = sc.nextLine();
            checker = LoginValidator.passwordChecker(password);
        } while(checker);


        //Write to the Logins.txt file the login information just created in a one line format
        String pattern = username + "_" + password + "_" + firstName + "_" + lastName + "\n";

        //Write the pattern of personal information to a central database of user accounts
        FileIO.write("AllUserAccounts", pattern);

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

        System.out.println("Congrats on your new account! Please log in");
    }

    public Customer loginCustomer() {

        Scanner sc1 = new Scanner(System.in);
        boolean lookupStatus;
        String accessor;
        String fileName;
        String fullFile = pathway + "AllCustomers";
        File file = new File(fullFile);

        do {
            System.out.println("\nPlease enter you username and password");
            System.out.print("Username: ");
            username = sc1.nextLine();
            System.out.print("Password: ");
            password = sc1.nextLine();
            String pattern = username + "_" + password;

            lookupStatus = FileIO.lookupLogin("AllUserAccounts", pattern);
            if(lookupStatus) {

                fileName = null;
                try(Scanner sc = new Scanner(file)) {
                    while(sc.hasNextLine()) {

                        accessor = sc.nextLine();
                        if(accessor.equals(username)) {

                            fileName = sc.nextLine();
                            break;
                        }
                    }
                } catch(FileNotFoundException e) {
                    System.out.println("There is no customer account with those credentials");
                    continue;
                }

                if(fileName == null) {
                    return null;
                }

                currentCustomer = FileIO.deSerialize(fileName, Customer.class);
                currentCustomer.loadAccounts();
                return currentCustomer;
            }
            else {
                System.out.println("Your username or password was incorrect. Please try again.");
                return null;
            }
        } while(currentCustomer == null);

        sc1.close();
        return null;
    }

    public Employee loginEmployee() {
        Scanner sc1 = new Scanner(System.in);
        boolean lookupStatus;
        String accessor;
        String fileName;

        String fullFile = pathway + "AllEmployees";
        File file = new File(fullFile);

        do {
            System.out.println("\nPlease enter you username and password");
            System.out.print("Username: ");
            username = sc1.nextLine();
            System.out.print("Password: ");
            password = sc1.nextLine();
            String pattern = username + "_" + password;

            lookupStatus = FileIO.lookupLogin("AllUserAccounts", pattern);
            if(lookupStatus) {

                fileName = null;
                try(Scanner sc = new Scanner(file)) {
                    while(sc.hasNextLine()) {

                        accessor = sc.nextLine();
                        if(accessor.equals(username)) {

                            fileName = sc.nextLine();
                            break;
                        }
                    }
                } catch(FileNotFoundException e) {
                    System.out.println("There is no employee account with those credentials");
                    continue;
                }

                if(fileName == null) {
                    return null;
                }

                currentEmployee = FileIO.deSerialize(fileName, Employee.class);
                return currentEmployee;
            }
            else {
                System.out.println("Your username or password was incorrect. Please try again.");
                return null;
            }
        } while(currentEmployee == null);



        sc1.close();
        return null;
    }

    public BankAdmin loginBankAdmin() {
        Scanner sc1 = new Scanner(System.in);
        boolean lookupStatus;
        String accessor;
        String fileName;
        String fullFile = pathway + "AllAdmins";
        File file = new File(fullFile);

        do {
            System.out.println("\nPlease enter you username and password");
            System.out.print("Username: ");
            username = sc1.nextLine();
            System.out.print("Password: ");
            password = sc1.nextLine();
            String pattern = username + "_" + password;

            lookupStatus = FileIO.lookupLogin("AllUserAccounts", pattern);
            if(lookupStatus) {

                fileName = null;
                try(Scanner sc = new Scanner(file)) {
                    while(sc.hasNextLine()) {

                        accessor = sc.nextLine();
                        if(accessor.equals(username)) {

                            fileName = sc.nextLine();
                            break;
                        }
                    }
                } catch(FileNotFoundException e) {
                    System.out.println("There is no admin account with those credentials");
                    continue;
                }

                if(fileName == null) {
                    return null;
                }

                currentBankAdmin = FileIO.deSerialize(fileName, BankAdmin.class);
                return currentBankAdmin;
            }
            else {
                System.out.println("Your username or password was incorrect. Please try again.");
                return null;
            }
        } while(currentBankAdmin == null);

        sc1.close();
        return null;
    }
}