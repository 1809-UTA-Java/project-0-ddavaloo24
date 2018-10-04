package com.revature.bank.people;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.revature.bank.screens.BankAdminScreens;
import com.revature.bank.dbs.FileIO;
import com.revature.bank.accounts.BankAccount;
import com.revature.bank.people.*;

/**
 * Creates an employee that can look at any customer's account information
 * as well as approve or deny new, pending accounts
 */
public class Employee extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String employeeID;

    //Default constructor that initializes string values and uses the super constructor
    public Employee( String username, String password, String firstName, String lastName ) {
        
        super( username, password, firstName, lastName);
        this.employeeID = "e" + ( int )( Math.random() * 9999999 ) + 1;
    }

    //Copy constructor
    public Employee( Employee e ) {

        super( e.username, e.password, e.firstName, e.lastName);
        this.employeeID = "e" + ( int )( Math.random() * 9999999 ) + 1;
    }

    //Load all customer accounts and place them within the employee's arraylist
    @SuppressWarnings("unchecked")
    public void loadAccounts() {
        File dir = new File(FileIO.pathway);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith("bank"));
        ArrayList<BankAccount> temp = new ArrayList<>();
        String accountIDs;

        if(files.length != 0) {
            for(File f:files) {
                accountIDs = f.getName();
                temp = (FileIO.deSerialize(accountIDs, ArrayList.class));
                myAccounts.addAll(temp);
            }
        }
    }

    //Search through customer acounts and view their personal and account information
    @SuppressWarnings("unchecked")
    public void findAllCustomerAccs(Scanner sc) {
        ArrayList<BankAccount> temp = new ArrayList<>();
        String accountIDs;
        File dir = new File(FileIO.pathway);
        File[] files = dir.listFiles((dir1, name) -> name.startsWith("c") && !name.endsWith("bank"));
        int choice;

        if(files.length == 0) {
            System.out.println("\nThere are no customer accounts");
            return;
        }

        System.out.println("\nWhich account would you like to see? Or press " + (files.length + 1) + " to go back.");
        for(int i = 0; i < files.length; i++) {
            System.out.println((i+1) + ". " + files[i].getName());
        }
        System.out.print("Choice: ");

        do {
            try{
                try{
                    choice = Integer.parseInt(sc.nextLine());
                } catch(NumberFormatException e) {
                    System.out.println("\nPlease choose from the above numbers");
                    continue;
                }

                if((choice < files.length + 2) && choice > 0) break;
                else System.out.println("Please choose from the above numbers");

            } catch(InputMismatchException e) {
                System.out.println("Please choose from the above numbers");
                sc.nextLine();
            }
        } while( true );

        choice = choice - 1;
        FileIO.deSerialize(files[choice].getName(), Customer.class).printMyInfo();
        
        File chosenAccount = new File(files[choice].getName() + "bank");
        accountIDs = chosenAccount.getName();

        //Clear the accounts to be able to list the customer's account information
        myAccounts.clear();

        temp = (FileIO.deSerialize(accountIDs, ArrayList.class));
        myAccounts.addAll(temp);

        System.out.println("\nApproved Accounts:");
        int i = 0;
        for(BankAccount bA : myAccounts) {
            if(bA.getStatus()) {
                i++;
                System.out.println(bA.toString());
            }
        }

        if(i == 0) System.out.println("None");

        System.out.println("\nPending Accounts");
        i = 0;
        for(BankAccount bA : myAccounts) {
            if(bA.getStatus() == false) {
                i++;
                System.out.println(bA.toString());
            }
        }

        if(i == 0) System.out.println("None");

        myAccounts.clear();
        this.loadAccounts();
    }

    //Approve or deny the pending accounts of customers
    public void approveOrDeny(Scanner sc) {
        String choice;

        if(myAccounts.isEmpty()) {
            System.out.println("\nThere are no pending accounts");
            return;
        }

        for(BankAccount bA : myAccounts) {
            if(bA.getStatus() == false) {
                do {
                    System.out.print("\nWould you like to approve or deny account " + bA.getBankID() + "\n" + 
                        "Choice: ");
                
                    choice = sc.nextLine().trim();
        
                    if(choice.equals("approve") || choice.equals("deny")) break;
                    else System.out.println("Sorry! I didn't get that! Please choose approve or deny");
                } while(true);
        
                if(choice.equals("approve")) {
                    //Approve the account
                    bA.setStatus(true);
                    System.out.println("\nApproved!");
                }
                else if(choice.equals("deny")) {
                    //Deny the account
                    String iD = bA.getBankID();
                    int index = myAccounts.indexOf(bA);

                    myAccounts.set(index, null);
                    BankAdminScreens.canceledOnes.add(index + 1);
                    System.out.println("\nDeny complete! Account " + iD + " has been removed");
                }
            }
        }
        this.writeAccounts();
        this.loadAccounts();
    }

    //Write all accounts in this arraylist to the separate accounts it got them from
    @SuppressWarnings("unchecked")
    public void writeAccounts() {
        File dir = new File(FileIO.pathway);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith("bank"));
        ArrayList<BankAccount> temp = new ArrayList<>();
        String accountIDs;
        int i = 0;

        if(files.length != 0) {
            for(File f:files) {
                accountIDs = f.getName();
                temp = (FileIO.deSerialize(accountIDs, ArrayList.class));

                for(int j = 0; j < temp.size(); j++) {
                    temp.set(j, myAccounts.get(i));
                    myAccounts.remove(i);
                }

                for(int j = 0; j < temp.size(); j++) {
                    if(temp.get(j) == null) temp.remove(j);
                }
                
                f.delete();
                FileIO.serialize(accountIDs, temp); 
            }
        }
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String applyForAcc() {
        return null;
    }
}