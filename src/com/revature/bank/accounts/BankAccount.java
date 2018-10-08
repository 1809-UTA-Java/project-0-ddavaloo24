package com.revature.bank.accounts;

import java.io.IOException;
import java.io.Serializable;
import java.lang.Math;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.revature.bank.util.ConnectionUtil;

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
    private boolean approved;


    //The default constructor setting all the fields
    public BankAccount(String lastName, double balance, boolean approved) {
        this.balance = balance;
        this.accID = lastName + ( int )( Math.random() * 9999 ) + 1;
        this.approved = approved;
    }

    public BankAccount(double balance, String accID, boolean approved) {
        this.balance = balance;
        this.accID = accID;
        this.approved = approved;
    }




    //Method for customer to remove money from account
    public void withdraw( double amt ) {
        if((balance - amt) < 0 || amt <= 0) System.out.println("\nSorry but the amount inputted is invalid");
        else {
            balance = (double) Math.round((balance - amt) * 100) / 100;;

            PreparedStatement ps = null;

            try(Connection conn = ConnectionUtil.getConnection()) {
                String sql = "INSERT INTO TRANSACTIONS VALUES (?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, accID);
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                ps.setString(3, "withdraw");
                ps.setDouble(4, amt);
                ps.execute();

                ps.close();

            } catch(SQLException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }

            System.out.println("\nWithdraw Successful! Your new balance is $" + balance);
        }
    }

    //Method to deposit money into account.
    public void deposit(double amt) {
        if(amt <= 0) {
            System.out.println("Invalid value");
            return;
        }
        balance = (double) Math.round((balance + amt) * 100) / 100;;

        PreparedStatement ps = null;

            try(Connection conn = ConnectionUtil.getConnection()) {
                String sql = "INSERT INTO TRANSACTIONS VALUES (?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, accID);
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                ps.setString(3, "deposit");
                ps.setDouble(4, amt);
                ps.execute();

                ps.close();

            } catch(SQLException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }

        System.out.println("\nDeposit Successful! Your new balance is $" + balance);
    }

    //Transfer money between the calling account and target account with the selected amount
    public void transfer(double amt, BankAccount target) {
        if(balance < amt || amt <= 0) {
            System.out.println("\nYou do not have enough funds to transfer.");
            return;
        }

        balance = (double) Math.round((balance - amt) * 100) / 100;
        target.balance = (double) Math.round((target.balance + amt) * 100) / 100;

        PreparedStatement ps = null;

            try(Connection conn = ConnectionUtil.getConnection()) {
                String sql = "INSERT INTO TRANSACTIONS VALUES (?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, accID);
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                ps.setString(3, "withdraw");
                ps.setDouble(4, amt);
                ps.execute();


                sql = "INSERT INTO TRANSACTIONS VALUES (?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, target.accID);
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                ps.setString(3, "deposit");
                ps.setDouble(4, amt);
                ps.execute();

                ps.close();

            } catch(SQLException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }

        System.out.println("\nTransfer Complete! " + getBankID() + " has a balance of $" + balance + 
                " and " + target.getBankID() + " has a balance of $" + target.balance);
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

    public double getBalance() {
        return balance;
    }

    //Method to set the approved status of the account
    public void setStatus(boolean status) {
        approved = status;
    }
}