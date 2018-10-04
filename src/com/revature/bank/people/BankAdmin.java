package com.revature.bank.people;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.revature.bank.dbs.FileIO;
import com.revature.bank.accounts.BankAccount;
import com.revature.bank.people.*;
import com.revature.bank.screens.BankAdminScreens;

/**
 * 
 * Creates the bank admin user. They have the ability to view all
 * accounts as well as the ability to approve or deny new accounts
 * and to cancel existing ones.
 */
public class BankAdmin extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String bankAdminID;

    //Default constructor that initializes string values and sets all bools to false
    public BankAdmin( String username, String password, String firstName, String lastName ) {
        
        super( username, password, firstName, lastName);
        this.bankAdminID = "b" + ( int )( Math.random() * 9999999 ) + 1;
    }

    //Clone constructor
    public BankAdmin( BankAdmin ba ) {

        super( ba.username, ba.password, ba.firstName, ba.lastName);
        this.bankAdminID = "b" + ( int )( Math.random() * 9999999 ) + 1;
    }

    //Cancels the bank account and sets the position in the myAccounts arraylist to null
    public void cancelBankAccount(Scanner sc) {

        int choice = 0;
        boolean arrIndex = true;
        ArrayList<Integer> approvedIndex =  this.displayMyAccs();
        int totalAccs = approvedIndex.size();

        if(totalAccs != 0) {
            //Ask which account they want to cancel
            System.out.println("\nWhich account would you like to cancel? Or press " + (totalAccs + 1) + " to go back");
            do {
                try{
                    try{
                        choice = Integer.parseInt(sc.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.println("\nPlease choose from the above numbers");
                        continue;
                    }
    
                    if((choice < (totalAccs + 2)) && choice > 0) {

                        if(BankAdminScreens.canceledOnes.size() == 0) break;
                        for(int i = 0; i < BankAdminScreens.canceledOnes.size(); i++) {

                            if(choice != BankAdminScreens.canceledOnes.get(i)) arrIndex = false;
                            else {
                                arrIndex = true;
                                break;
                            }
                        }

                        if(arrIndex == true) System.out.println("That account was already canceled. Please try a different number");
                    }
                    else System.out.println("Please choose from the above numbers");
    
                } catch(InputMismatchException e) {
                    System.out.println("Please choose from the above numbers");
                    sc.nextLine();
                }
            } while(arrIndex);

            if(choice == (totalAccs + 1)) return;
            else {
                int index = choice - 1;
                String iD = myAccounts.get(index).getBankID();

                myAccounts.set(index, null);
                BankAdminScreens.canceledOnes.add(index + 1);

                System.out.println("Cancelation complete! Account " + iD + " has been removed");
            }
        }
    }

    //Lets the admin go through each pending account one by one and make a choice
    public void approveOrDeny(Scanner sc) {
        String choice;

        if(myAccounts.isEmpty()) {
            System.out.println("\nThere are no pending accounts");
            return;
        }


        for(BankAccount bA : myAccounts) {
            if(bA.getStatus() == false) {
                do {
                    System.out.print("Would you like to approve or deny account " + bA.getBankID() + "\n" + 
                        "Choice: ");
                
                    choice = sc.nextLine();
        
                    if(choice.equals("approve") || choice.equals("deny")) break;
                    else System.out.println("Sorry! I didn't get that! Please choose approve or deny");
                } while(true);
        
                if(choice.equals("approve")) {
                    //Approve the account
                    bA.setStatus(true);
                    System.out.println("\nApproved!\n");
                }
                else if(choice.equals("deny")) {
                    //Deny the account
                    String iD = bA.getBankID();
                    int index = myAccounts.indexOf(bA);

                    myAccounts.set(index, null);
                    BankAdminScreens.canceledOnes.add(index + 1);
                    System.out.println("\nDeny complete! Account " + iD + " has been removed\n");
                }
            }
        }
        this.writeAccounts();
        this.loadAccounts();
    }

    //Load up all created bank accounts into the admin's account arraylist
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

    //Writes all accounts to a file and empties the admin's arraylist
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

    public String getBankAdminID() {
        return bankAdminID;
    }

    public String applyForAcc() {
        return null;
    }
}