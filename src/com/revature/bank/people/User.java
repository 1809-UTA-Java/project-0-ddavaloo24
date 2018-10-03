package com.revature.bank.people;

import java.io.Serializable;
import java.util.ArrayList;
import com.revature.bank.accounts.BankAccount;

public abstract class User implements Serializable{

    private static final long serialVersionUID = 1L;
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String password;
    protected ArrayList<BankAccount> myAccounts = new ArrayList<>();

    public User( String username, String password, String firstName, String lastName) {
                this.username = username;
                this.password = password;
                this.firstName = firstName;
                this.lastName = lastName;
            }

    public ArrayList<Integer> displayMyAccs() {
        int i = 0;
        int approvedTotal = 0;
        ArrayList<Integer> approvedIndex = new ArrayList<>();

        if( myAccounts.isEmpty() ) {
            System.out.println("You have no accounts.\n");
        }
        else {
            //Print all approved accounts
            System.out.println("Approved accounts: ");
            for(BankAccount bA : myAccounts) {
                if(bA != null) {
                    if(bA.getStatus()) {
                        approvedTotal++;
                        approvedIndex.add(myAccounts.indexOf(bA));
                        System.out.println(approvedTotal + ". " + bA.toString());
                    }
                }
            }

            System.out.println(approvedIndex);

            if(approvedTotal == 0) {
                System.out.println("There are no approved accounts");
            }

            //Print pending accounts
            System.out.println("\nPending accounts: ");
            for(BankAccount bA : myAccounts) {
                if(bA != null) {
                    if(bA.getStatus() == false) {
                        i++;
                        System.out.println(i + ". " + bA.toString());
                    }
                }
            }

            if(i == 0) {
                System.out.println("There are no pending accounts");
            }

            System.out.print("\n");
        }

        return approvedIndex;
    }

    public String toString() {
        return firstName + " " + lastName;
    }

    public BankAccount getAccount(int index) {
        return myAccounts.get(index);
    }

    public abstract void loadAccounts();
    public abstract void writeAccounts();
    public abstract String applyForAcc();
}