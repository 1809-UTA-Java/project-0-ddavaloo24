package com.revature.bank.people;

import java.io.Serializable;
import java.util.ArrayList;
import com.revature.bank.accounts.BankAccount;

public abstract class User implements Serializable{
    protected boolean accessToCustomerInfo;
    protected boolean ableToAcceptAccs;
    protected boolean ableToCancelAccs;
    protected transient String firstName;
    protected transient String lastName;
    protected transient String username;
    protected transient String password;
    protected transient ArrayList<BankAccount> myAccounts = new ArrayList<>();

    public User( String username, String password, String firstName, String lastName,
            boolean accessToCustomerInfo, boolean ableToAcceptAccs, boolean ableToCancelAccs ) {
                this.username = username;
                this.password = password;
                this.firstName = firstName;
                this.lastName = lastName;
                this.accessToCustomerInfo = accessToCustomerInfo;
                this.ableToAcceptAccs = ableToAcceptAccs;
                this.ableToCancelAccs = ableToCancelAccs;
            }

    public void displayMyAccs() {
        System.out.println("Your available accounts are:");

        if( myAccounts.isEmpty() ) {
            System.out.println("You have no accounts");
        }
        else {
            for(BankAccount bA : myAccounts) {
                bA.toString();
            }
        }
    }

    public abstract void applyForAcc();
    public String toString() {
        return firstName + " " + lastName;
    }
}