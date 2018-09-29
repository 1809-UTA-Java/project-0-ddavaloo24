package com.revature.bank.people;

import com.revature.bank.accounts.BankAccount;
import com.revature.bank.people.*;

public class BankAdmin extends User {

    public String bankAdminID;

    //Default constructor that initializes string values and sets all bools to false
    public BankAdmin( String username, String password, String firstName, String lastName ) {
        
        super( username, password, firstName, lastName, true, true, true );
        this.bankAdminID = "ba" + ( int )( Math.random() * 9999 ) + 1;
    }

    public BankAdmin( BankAdmin ba ) {

        super( ba.username, ba.password, ba.firstName, ba.lastName, true, true, true );
        this.bankAdminID = "ba" + ( int )( Math.random() * 9999 ) + 1;
    }

    public void applyForAcc() {
        BankAccount newBA = new BankAccount(username, firstName, lastName, 0, true);
        myAccounts.add(newBA);
    }
}