package com.revature.bank.accounts;

import java.lang.Math;

public class BankAccount {

    public String accID;
    private String username;
    private String firstName;
    private String lastName;
    private double balance;
    public boolean approved;

    //TODO: Create a constructor
    //TODO: Make the AccID a unqiue number and have it be randomized
    public BankAccount( String username, String firstName, String lastName, double balance, boolean approved ) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.accID = lastName + ( int )( Math.random() * 99 ) + 1;
        this.approved = approved;

    }


    //Method for customer to remove money from account. Will not work if the amount being
    //withdrawn is more than amount in account. Also tells you new balance.
    public void withdraw( double amt ) {
        
        if( (balance - amt) < 0 ) {
            System.out.println("Sorry but you cannot withdraw more than the amount in your account!");
        }
        else {
            balance = balance - amt;
            System.out.println("Withdraw Successful! Your new balance is " + balance);

        }
    }

    //Method to deposit money into account.
    public void deposit( double amt ) {
        balance = balance + amt;
        System.out.println("Deposit Successful! Your new balance is " + balance);
    }

    //TODO: Find out how to transfer money between two accounts
    public void transfer( double amt, int accID ) {

    }

    public String toString() {
        return (accID + " " +balance + "\n");
    }


}