package com.revature.bank.people;

import com.revature.bank.accounts.BankAccount;
import com.revature.bank.people.*;

public class Customer extends User {
    
    public int customerID;

    //Default constructor that initializes string values and sets all bools to false
    public Customer( String username, String password, String firstName, String lastName ) {
        
        super( username, password, firstName, lastName, false, false, false );
        this.customerID = ( int )( Math.random() * 999999 ) + 1;
    }
    public Customer( Customer c ) {
        super( c.username, c.password, c.firstName, c.lastName, false, false, false );
        this.customerID = ( int )( Math.random() * 999999 ) + 1;
    }

    public void applyForAcc() {
        BankAccount newBA = new BankAccount(username, firstName, lastName, 0, false);
        myAccounts.add(newBA);
    }
}