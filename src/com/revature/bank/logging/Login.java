package com.revature.bank.logging;

import java.io.Console;
import java.util.*;
import com.revature.bank.accounts.*;
import com.revature.bank.people.*;
import com.revature.bank.dbs.*;


public class Login {

    //Hashmap that holds a username to object relationship
    protected HashMap<String, Customer> allCustomers = new HashMap<>();
    protected HashMap<String, Employee> allEmployees = new HashMap<>();
    protected HashMap<String, BankAdmin> allAdmins = new HashMap<>();

    //Holds all logins with username and password
    protected HashMap<String, String> allLogins = new HashMap<>();

    //Holds the username and the type of account (1 for customer, 2 for employee, 3 for admin)
    protected HashMap<String, Integer> accountTypes = new HashMap<>();

    protected User currentUser;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;

    //Used for checking account creation checks
    protected boolean checker;

    public void createAccount(int type) {
        Scanner sc = new Scanner( System.in );

        //Ask the user for account information
        System.out.println("Please fill out the following to create an account:");

        //First name request and check
        System.out.print("First name must be 2-20 characters with no numbers or special characters \n" +        
                    "First name : ");
        do {
            firstName = sc.nextLine();
            checker = LoginValidator.nameChecker(firstName);
            System.out.print("First name: ");
        } while(checker);

        //Last name request and check
        System.out.print("Last name must be 2-20 characters with no numbers or special characters \n" +
                    "Last name: ");
        do {
            lastName = sc.nextLine();
            checker = LoginValidator.nameChecker(lasttName);
            System.out.print("Last name: ");
        } while(checker);

        System.out.print("Username must be 8-12 characters and cannot include underscores \n" +
                    "Username : ");
        username = sc.nextLine();

        System.out.print("Password must be 8-12 characters and cannot include underscores \n" +
                    "Password: ");
        password = sc.nextLine();

        //Write to the Logins.txt file the login information just created in a one line format
        String pattern = username + "_" + password + "_" + firstName + "_" + lastName + "\n";

        switch(type) {
            case 1:
            FileIO.write("AllCustomers", pattern);
            break;

            case 2:
            FileIO.write("AllEmployees", pattern);
            break;

            case 3:
            FileIO.write("AllAdmins", pattern);
            break;

            default:
            System.out.println("Invalid person");
        }

        //Create a new customer object and place it into the customer hashmap
        FileIO.write("Logins", pattern);

        System.out.println("Congrats on your new account! Please log in");
    }

    public Customer loginCustomer() {
        Scanner sc1 = new Scanner( System.in );

        do {

            System.out.println( "Please enter you username and password" );
            System.out.print( "Username: " );
            username = sc1.nextLine();
            System.out.print("Password: ");
            password = sc1.nextLine();

            if ( allLogins.get(username).equals(password) ) {

                return allCustomers.get(username);
            }
            else {
                System.out.println( "Wrong information entered" );
            }

        } while(currentUser == null);

        sc1.close();
        return null;
    }

    public Employee loginEmployee() {
        Scanner sc = new Scanner( System.in );

        do {

            System.out.println( "Please enter you username and password" );
            System.out.print( "Username: " );
            username = sc.nextLine();
            System.out.print("Password: ");
            password = sc.nextLine();

            if ( allLogins.get(username).equals(password) ) {

                return allEmployees.get(username);
            }
            else {
                System.out.println( "Wrong information entered" );
            }

        } while(currentUser == null);

        sc.close();
        return null;
    }

    public BankAdmin loginBankAdmin() {
        Scanner sc = new Scanner( System.in );

        do {

            System.out.println( "Please enter you username and password" );
            System.out.print( "Username: " );
            username = sc.nextLine();
            System.out.print("Password: ");
            password = sc.nextLine();

            if ( allLogins.get(username).equals(password) ) {

                return allAdmins.get(username);
            }
            else {
                System.out.println( "Wrong information entered" );
            }

        } while(currentUser == null);

        sc.close();
        return null;
    }

}