package com.revature.bank.accounts;

import java.io.Serializable;
import java.lang.Math;

/**
 * 
 * This class is used to create a bank account as well as 
 * move money into, from, and to other accounts
 */
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    private String accID;
    private double balance;

    //Default for all new accounts
    private boolean approved = false;

    //The default constructor setting all the fields
    public BankAccount(String lastName, double balance) {
        this.balance = balance;
        this.accID = lastName + ( int )( Math.random() * 9999 ) + 1;
    }

    

    //Method for customer to remove money from account
    public void withdraw( double amt ) {
        if((balance - amt) < 0) System.out.println("\nSorry but you cannot withdraw more than the amount in your account!");
        else {
            balance = balance - amt;
            System.out.println("\nWithdraw Successful! Your new balance is $" + balance);
        }
    }

    //Method to deposit money into account.
    public void deposit(double amt) {
        balance = balance + amt;
        System.out.println("\nDeposit Successful! Your new balance is $" + balance);
    }

    //Transfer money between the calling account and target account with the selected amount
    public void transfer(double amt, BankAccount target) {
        if(balance < amt) {
            System.out.println("\nYou do not have enough funds to transfer.");
            return;
        }
        balance = balance - amt;
        target.balance = target.balance + amt;
        System.out.println("\nTransfer Complete! " + getBankID() + " has a balance of " + balance + 
                " and " + target.getBankID() + " has a balance of " + target.balance);
    }

    //Overriden to print the account number and corresponding balance
    public String toString() {
        return (accID + " has a balance of $" + balance);
    }

    //Getter method for retrieving the ID of the current bank account
    public String getBankID() {
        return accID;
    }

    //Method to return the approved status of the account
    public boolean getStatus() {
        return approved;
    }

    
    //Method to set the approved status of the account
    public void setStatus(boolean status) {
        approved = status;
    }
}