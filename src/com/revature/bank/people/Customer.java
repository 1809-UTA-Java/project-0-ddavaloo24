package com.revature.bank.people;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;

import com.revature.bank.accounts.BankAccount;
import com.revature.bank.dbs.FileIO;
import com.revature.bank.people.*;

public class Customer extends User implements Serializable{
    
    private String customerID;

    //Default constructor that initializes string values and sets all bools to false
    public Customer( String username, String password, String firstName, String lastName ) {
        super( username, password, firstName, lastName, false, false, false );
        this.customerID = "c" + ( int )( Math.random() * 9999999 ) + 1;
    }
    public Customer( Customer c ) {
        super( c.username, c.password, c.firstName, c.lastName, false, false, false );
        this.customerID = "c" + ( int )( Math.random() * 9999999 ) + 1;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String applyForAcc() {
        BankAccount newBA = new BankAccount(username, firstName, lastName, 0.00);
        myAccounts.add(newBA);
        return newBA.getBankID();
    }

    public void loadAccounts() {
        File dir = new File(FileIO.pathway);
        File[] files = dir.listFiles((dir1, name) -> name.startsWith(getCustomerID() + "bank"));

        if(files.length != 0) {
            myAccounts = FileIO.deSerialize(files[0].getName(), ArrayList.class);
        }
    }

    public void writeAccounts() {
        String newFileName = getCustomerID() + "bank";
        FileIO.serialize(newFileName, myAccounts);
    }

    public BankAccount getAccount(int index) {
        return myAccounts.get(index);
    }
}