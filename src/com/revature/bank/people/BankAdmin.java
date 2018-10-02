package com.revature.bank.people;

import java.io.Serializable;
import java.util.ArrayList;

import com.revature.bank.accounts.BankAccount;
import com.revature.bank.people.*;

public class BankAdmin extends User implements Serializable {

    private String bankAdminID;

    //Default constructor that initializes string values and sets all bools to false
    public BankAdmin( String username, String password, String firstName, String lastName ) {
        
        super( username, password, firstName, lastName, true, true, true );
        this.bankAdminID = "b" + ( int )( Math.random() * 9999999 ) + 1;
    }

    public BankAdmin( BankAdmin ba ) {

        super( ba.username, ba.password, ba.firstName, ba.lastName, true, true, true );
        this.bankAdminID = "b" + ( int )( Math.random() * 9999999 ) + 1;
    }

    public String getBankAdminID() {
        return bankAdminID;
    }

    public String applyForAcc() {
        BankAccount newBA = new BankAccount(username, firstName, lastName, 0);
        myAccounts.add(newBA);
        return newBA.getBankID();
    }

    public void loadAccounts(){
    }
    public void writeAccounts(){}
}