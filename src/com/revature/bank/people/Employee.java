package com.revature.bank.people;

import com.revature.bank.accounts.BankAccount;
import com.revature.bank.people.*;

public class Employee extends User {
    public String employeeID;

    //Default constructor that initializes string values and uses the super constructor
    public Employee( String username, String password, String firstName, String lastName ) {
        
        super( username, password, firstName, lastName, true, true, false );
        this.employeeID = "e" + ( int )( Math.random() * 99999 ) + 1;
    }

    public Employee( Employee e ) {

        super( e.username, e.password, e.firstName, e.lastName, true, true, false );
        this.employeeID = "e" + ( int )( Math.random() * 99999 ) + 1;
    }

    public void applyForAcc() {
        BankAccount newBA = new BankAccount(username, firstName, lastName, 0, true);
        myAccounts.add(newBA);
    }
}