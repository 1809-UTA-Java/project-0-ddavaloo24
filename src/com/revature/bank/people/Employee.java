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
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

import com.revature.bank.screens.BankAdminScreens;
import com.revature.bank.util.ConnectionUtil;
import com.revature.bank.dbs.FileIO;
import com.revature.bank.accounts.BankAccount;
import com.revature.bank.people.*;

/**
 * Creates an employee that can look at any customer's account information
 * as well as approve or deny new, pending accounts
 */
public class Employee extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String employeeID;



    //Default constructor that initializes string values and uses the super constructor
    public Employee( String username, String password, String firstName, String lastName ) {
        
        super(username, password, firstName, lastName);
        this.employeeID = "e" + ( int )( Math.random() * 9999999 ) + 1;
    }

    //Copy constructor
    public Employee( Employee e ) {

        super(e.username, e.password, e.firstName, e.lastName);
        this.employeeID = "e" + ( int )( Math.random() * 9999999 ) + 1;
    }

    public Employee(String username, String password, String firstName, String lastName, String accID) {
        super(username, password, firstName, lastName);
        this.employeeID = accID;
    }

    

    //Load all customer accounts and place them within the employee's arraylist
    public void loadAccounts() {
        PreparedStatement ps = null;

        try(Connection conn = ConnectionUtil.getConnection()) {
            
            String sql = "SELECT * FROM BANKACCOUNTS";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                if( rs.getString("APPROVED").equals("n")) {
                    BankAccount loader = new BankAccount(rs.getDouble("BALANCE"), rs.getString("B_ID"), false);
                    myAccounts.add(loader);
                }
                else {
                    BankAccount loader = new BankAccount(rs.getDouble("BALANCE"), rs.getString("B_ID"), true);
                    myAccounts.add(loader);
                }
            }

            rs.close();
            ps.close();

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //Search through customer acounts and view their personal and account information
    public void findAllCustomerAccs(Scanner sc) {

        ArrayList<String> allCustAccounts = new ArrayList<>();
        PreparedStatement ps = null;
        int choice;

        try(Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT C_ID FROM CUSTOMERS";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                allCustAccounts.add(rs.getString("C_ID"));
            }

            if(allCustAccounts.isEmpty()) {
                System.out.println("\n----------------------------------------------------");
                System.out.println("There are no customer accounts");
                return;
            }

            System.out.println("\n----------------------------------------------------");
            System.out.println("Which account would you like to see? Or press " + (allCustAccounts.size() + 1) + " to go back.");
            for(int i = 0; i < allCustAccounts.size(); i++) {
                System.out.println((i+1) + ". " + allCustAccounts.get(i));
            }
            System.out.print("Choice: ");

            do {
                try{
                    try{
                        choice = Integer.parseInt(sc.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.println("\nPlease choose from the above numbers");
                        continue;
                    }

                    if((choice < allCustAccounts.size() + 2) && choice > 0) break;
                    else System.out.println("Please choose from the above numbers");

                } catch(InputMismatchException e) {
                    System.out.println("Please choose from the above numbers");
                    sc.nextLine();
                }
            } while( true );

            choice = choice - 1;

            sql = "SELECT * FROM CUSTOMERS WHERE C_ID = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, allCustAccounts.get(choice));
            rs = ps.executeQuery();

            while(rs.next()) {
                System.out.println("\n" + rs.getString("C_ID") + "'s account information: \n" +
                    "Username: " + rs.getString("C_USERNAME") + "\n" +
                    "First name: " + rs.getString("C_FNAME") + "\n" +
                    "Last Name: " + rs.getString("C_LNAME"));
            }

            myAccounts.clear();

            sql = "SELECT bankaccounts.B_ID, BALANCE, APPROVED " +
                "FROM customers, customerbankconn, bankaccounts " +
                "WHERE customers.c_id = customerbankconn.c_id " +
                "AND bankaccounts.b_id = customerbankconn.b_id " +
                "AND customers.c_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, allCustAccounts.get(choice));
            rs = ps.executeQuery();

            while(rs.next()) {
                if( rs.getString("APPROVED").equals("n")) {
                    BankAccount loader = new BankAccount(rs.getDouble("BALANCE"), rs.getString("B_ID"), false);
                    myAccounts.add(loader);
                }
                else {
                    BankAccount loader = new BankAccount(rs.getDouble("BALANCE"), rs.getString("B_ID"), true);
                    myAccounts.add(loader);
                }
            }

            System.out.println("\nApproved Accounts:");
            int i = 0;
            for(BankAccount bA : myAccounts) {
                if(bA.getStatus()) {
                    i++;
                    System.out.println(bA.toString());
                }
            }
            if(i == 0) System.out.println("None");

            System.out.println("\nPending Accounts");
            i = 0;
            for(BankAccount bA : myAccounts) {
                if(bA.getStatus() == false) {
                    i++;
                    System.out.println(bA.toString());
                }
            }
            if(i == 0) System.out.println("None");

            myAccounts.clear();
            this.loadAccounts();

            rs.close();
            ps.close();

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //Approve or deny the pending accounts of customers
    public void approveOrDeny(Scanner sc) {
        String choice;
        PreparedStatement ps = null;
        String sql = null;
        int i = 0;

        if(myAccounts.isEmpty()) {
            System.out.println("\nThere are no pending accounts");

            return;
        }

        for(BankAccount bA : myAccounts) {
            if(bA.getStatus() == false) {
                i++;
                do {
                    System.out.println("\n----------------------------------------------------");
                    System.out.print("Would you like to approve or deny account " + bA.getBankID() + "\n" + 
                        "Choice: ");
                
                    choice = sc.nextLine().trim();
        
                    if(choice.equals("approve") || choice.equals("deny")) break;
                    else System.out.println("Sorry! I didn't get that! Please choose approve or deny");
                } while(true);
        
                if(choice.equals("approve")) {
                    //Approve the account
                    bA.setStatus(true);

                    try(Connection conn = ConnectionUtil.getConnection()) {
                        sql = "UPDATE BANKACCOUNTS SET APPROVED = 'y' WHERE B_ID = ?";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, bA.getBankID());
                        ps.execute();

                        ps.close();
                    } catch(SQLException e) {
                        e.printStackTrace();
                    } catch(IOException e) {
                        e.printStackTrace();
                    } 

                    System.out.println("\nApproved!");
                }
                else if(choice.equals("deny")) {
                    
                    //Deny the account
                    try(Connection conn = ConnectionUtil.getConnection()) {

                        sql = "DELETE FROM CUSTOMERBANKCONN WHERE B_ID = ?";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, bA.getBankID());
                        ps.execute();
              
                        sql = "DELETE FROM BANKACCOUNTS WHERE B_ID = ?";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, bA.getBankID());
                        ps.execute();

                        myAccounts.set(myAccounts.indexOf(bA), null);

                        System.out.println("\nDeny complete! Account " + bA.getBankID() + " has been removed");
            
                        ps.close();
                    } catch(SQLException e) {
                        e.printStackTrace();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }   
                }
            }
        }

        if(i == 0) System.out.println("\nThere are no pending accounts\n");

        Iterator<BankAccount> accIt = myAccounts.iterator();
        while(accIt.hasNext()) {
            if(accIt.next() == null) accIt.remove();
        }
    }

    //Write all accounts in this arraylist to the separate accounts it got them from
    public void writeAccounts() {

        PreparedStatement ps = null;
        String sql = null;

        try(Connection conn = ConnectionUtil.getConnection()) {
  
            for(BankAccount bankAcc : myAccounts) {
                sql = "UPDATE BANKACCOUNTS SET BALANCE=?, APPROVED=? WHERE B_ID=?";
                ps = conn.prepareStatement(sql);

                ps.setDouble(1, bankAcc.getBalance());
                ps.setString(3, bankAcc.getBankID());

                if(bankAcc.getStatus()) ps.setString(2, "y");
                else ps.setString(2, "n");
                
                ps.execute();
            }

            ps.close();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }   

    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String applyForAcc() {
        return null;
    }
}