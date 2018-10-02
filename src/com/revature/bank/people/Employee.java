package com.revature.bank.people;

import java.io.Serializable;
import java.util.ArrayList;

import com.revature.bank.accounts.BankAccount;
import com.revature.bank.people.*;

public class Employee extends User implements Serializable {
    
    private String employeeID;

    //Default constructor that initializes string values and uses the super constructor
    public Employee( String username, String password, String firstName, String lastName ) {
        
        super( username, password, firstName, lastName, true, true, false );
        this.employeeID = "e" + ( int )( Math.random() * 9999999 ) + 1;
    }

    public Employee( Employee e ) {

        super( e.username, e.password, e.firstName, e.lastName, true, true, false );
        this.employeeID = "e" + ( int )( Math.random() * 9999999 ) + 1;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String applyForAcc() {
        BankAccount newBA = new BankAccount(username, firstName, lastName, 0);
        myAccounts.add(newBA);
        return newBA.getBankID();
    }

    public void loadAccounts(){
        
    }
    public  void writeAccounts(){}
}