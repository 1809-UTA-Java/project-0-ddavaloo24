package com.revature.bank.logging;

import java.io.Console;
import java.io.FileReader;
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

    protected Customer currentCustomer = null;
    protected Employee currentEmployee = null;
    protected BankAdmin currentBankAdmin = null;

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
        do {
            System.out.print("First name must be 2-20 characters with no numbers or special characters \n" +        
                    "First name : ");
            firstName = sc.nextLine().trim();
            checker = LoginValidator.nameChecker(firstName);
        } while(checker);
        checker = true;

        //Last name request and check        
        do {
            System.out.print("Last name must be 2-20 characters with no numbers or special characters \n" +
                    "Last name: ");
            lastName = sc.nextLine().trim();
            checker = LoginValidator.nameChecker(lastName);
        } while(checker);
        checker = true;

        //Username request and check
        do {
            System.out.print("Username must be 6-12 characters and cannot include underscores or spaces \n" +
                        "Username : ");
            username = sc.nextLine();
            checker = LoginValidator.usernameChecker(username);
        } while(checker);
        checker = true;

        //Password request and check
        do{
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
            //Create a new customer and add to a doc that holds the username and customerID
            //Also create a file that is titled the customerID and holds the customer object
            case 1:
            Customer c = new Customer(username, password, firstName, lastName);
            pattern = username + "\n" + c.getCustomerID() + "\n";
            FileIO.write("AllCustomers", pattern);
            FileIO.serialize(c.getCustomerID(), c);
            break;

            //Create a new employee and store it into a file titled its employeeID
            //Also add to file that holds all employee accounts using username and employeeID
            case 2:
            Employee e = new Employee(username, password, firstName, lastName);
            pattern = username + "\n" + e.getEmployeeID() + "\n";
            FileIO.write("AllEmployees", pattern);
            FileIO.serialize(e.getEmployeeID(), e);
            break;

            //Create a new admin and serialize it into a file using the AdminID
            //Add the admin username and ID to a database for admin
            case 3:
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
        String pattern;
        boolean lookupStatus;
        String fileName;

        do {

            System.out.println( "Please enter you username and password" );
            System.out.print( "Username: " );
            username = sc1.nextLine();
            System.out.print("Password: ");
            password = sc1.nextLine();

            pattern = username + "_" + password;

            lookupStatus = FileIO.lookupLogin("AllUserAccounts", pattern);
            if(lookupStatus) {

                fileName = null;

                Scanner sc = new Scanner("AllCustomers");
                while(sc.hasNextLine()) {

                    sc.nextLine();
                    if(sc.equals(username)) {
                        fileName = sc.nextLine();
                        break;
                    }
                }
                sc.close();

                currentCustomer = FileIO.deSerialize(fileName);
                return currentCustomer;
            }
            else {
                System.out.println("Your username or password was incorrect. Please try again.");
            }
        } while(currentCustomer == null);

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

        } while(currentEmployee == null);

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

        } while(currentBankAdmin == null);

        sc.close();
        return null;
    }

}