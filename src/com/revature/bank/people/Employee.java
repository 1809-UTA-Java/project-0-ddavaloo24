package com.revature.bank.people;

import java.io.Serializable;

import com.revature.bank.accounts.BankAccount;
import com.revature.bank.people.*;

public class Employee extends User implements Serializable {
    
    private transient String employeeID;

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

    public void applyForAcc() {
        BankAccount newBA = new BankAccount(username, firstName, lastName, 0, true);
        myAccounts.add(newBA);
    }
}