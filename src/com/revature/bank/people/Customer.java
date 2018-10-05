package com.revature.bank.people;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.bank.accounts.BankAccount;
import com.revature.bank.dbs.FileIO;
import com.revature.bank.people.*;
import com.revature.bank.util.ConnectionUtil;

/**
 * Creates a customer that has the ability to create a bank account
 * (pending approval from an employee or admin) and deposit, withdraw,
 * and transfer money between its accounts
 */
public class Customer extends User implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private String customerID;



    //Default constructor that initializes string values and sets all bools to false
    public Customer( String username, String password, String firstName, String lastName ) {
        super( username, password, firstName, lastName);
        this.customerID = "c" + ( int )( Math.random() * 9999999 ) + 1;
    }

    public Customer(String username, String password, String firstName, String lastName, String accID) {
        super(username, password, firstName, lastName);
        this.customerID = accID;
    }

    //Clone constructor
    public Customer( Customer c ) {
        super( c.username, c.password, c.firstName, c.lastName);
        this.customerID = "c" + ( int )( Math.random() * 9999999 ) + 1;
    }






//Create a pending account
public String applyForJointAcc(String secondCID) {

    PreparedStatement ps = null;
    String sql = null;

    BankAccount newBA = new BankAccount(lastName, 0);
    myAccounts.add(newBA);  

    try(Connection conn = ConnectionUtil.getConnection()) {

        sql = "INSERT INTO BANKACCOUNTS VALUES(?, ?, 'n')";
        ps = conn.prepareStatement(sql);
        ps.setString(1, newBA.getBankID());
        ps.setDouble(2, newBA.getBalance());
        ps.execute();

        sql = "INSERT INTO CUSTOMERBANKCONN VALUES(?, ?)";
        ps = conn.prepareStatement(sql);
        ps.setString(1, customerID);
        ps.setString(2, newBA.getBankID());
        ps.execute();

        sql = "INSERT INTO CUSTOMERBANKCONN VALUES(?, ?)";
        ps = conn.prepareStatement(sql);
        ps.setString(1, secondCID);
        ps.setString(2, newBA.getBankID());
        ps.execute();

        ps.close();
    } catch(SQLException e) {
        e.printStackTrace();
    } catch(IOException e) {
        e.printStackTrace();
    }
    
    return newBA.getBankID();

}













    //Create a pending account
    public String applyForAcc() {

        PreparedStatement ps = null;
        String sql = null;

        BankAccount newBA = new BankAccount(lastName, 0);
        myAccounts.add(newBA);  

        try(Connection conn = ConnectionUtil.getConnection()) {

            sql = "INSERT INTO BANKACCOUNTS VALUES(?, ?, 'n')";
            ps = conn.prepareStatement(sql);
            ps.setString(1, newBA.getBankID());
            ps.setDouble(2, newBA.getBalance());
            ps.execute();

            sql = "INSERT INTO CUSTOMERBANKCONN VALUES(?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, customerID);
            ps.setString(2, newBA.getBankID());
            ps.execute();

            ps.close();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return newBA.getBankID();

    }

    //Load accounts from the personal file to arraylist
    public void loadAccounts() {

        PreparedStatement ps = null;

        try(Connection conn = ConnectionUtil.getConnection()) {
  
            String sql = "SELECT bankaccounts.B_ID, BALANCE, APPROVED " +
                "FROM customers, customerbankconn, bankaccounts " +
                "WHERE customers.c_id = customerbankconn.c_id " +
                "AND bankaccounts.b_id = customerbankconn.b_id " +
                "AND customers.c_id = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, customerID);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                BankAccount loader = new BankAccount(rs.getDouble("BALANCE"), rs.getString("B_ID"));
                myAccounts.add(loader);
            }

            ps.close();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //Write accounts to a personal file
    public void writeAccounts() {

        PreparedStatement ps = null;
        String sql = null;

        try(Connection conn = ConnectionUtil.getConnection()) {
  
            for(BankAccount bankAcc : myAccounts) {
                sql = "UPDATE BANKACCOUNTS SET BALANCE=? WHERE B_ID=?";
                ps = conn.prepareStatement(sql);
                ps.setDouble(1, bankAcc.getBalance());
                ps.setString(2, bankAcc.getBankID());
                ps.execute();
            }

            ps.close();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }   
    }

    //Prints the user infor. To be used by the employee
    public void printMyInfo() {
        System.out.println("\n" + getCustomerID() + "'s account information: \n" +
                "Username: " + username + "\n" +
                "First name: " + firstName + "\n" +
                "Last Name: " + lastName);
    }

    public String getCustomerID() {
        return customerID;
    }
}