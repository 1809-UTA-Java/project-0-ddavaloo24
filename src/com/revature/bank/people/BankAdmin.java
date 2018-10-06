package com.revature.bank.people;

import java.io.*;
import java.util.*;
import java.sql.*;

import com.revature.bank.dbs.FileIO;
import com.revature.bank.accounts.BankAccount;
import com.revature.bank.people.*;
import com.revature.bank.screens.BankAdminScreens;
import com.revature.bank.util.ConnectionUtil;

/**
 * 
 * Creates the bank admin user. They have the ability to view all
 * accounts as well as the ability to approve or deny new accounts
 * and to cancel existing ones.
 */
public class BankAdmin extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String bankAdminID;



    
    //Default constructor that initializes string values and sets all bools to false
    public BankAdmin( String username, String password, String firstName, String lastName ) {
        
        super( username, password, firstName, lastName);
        this.bankAdminID = "b" + ( int )( Math.random() * 9999999 ) + 1;
    }

    //Clone constructor
    public BankAdmin( BankAdmin ba ) {

        super( ba.username, ba.password, ba.firstName, ba.lastName);
        this.bankAdminID = "b" + ( int )( Math.random() * 9999999 ) + 1;
    }

    public BankAdmin(String username, String password, String firstName, String lastName, String accID) {
        super(username, password, firstName, lastName);
        this.bankAdminID = accID;
    }




    //Cancels the bank account and sets the position in the myAccounts arraylist to null
    public void cancelBankAccount(Scanner sc) {

        int choice = 0;
        boolean arrIndex = true;
        ArrayList<Integer> approvedIndex =  this.displayMyAccs();
        int totalAccs = approvedIndex.size();

        if(totalAccs != 0) {
            //Ask which account they want to cancel
            System.out.print("\nWhich account would you like to cancel? Or press " + (totalAccs + 1) + " to go back \n" + 
                    "Choice: ");
            do {
                try{
                    try{
                        choice = Integer.parseInt(sc.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.println("\nPlease choose from the above numbers");
                        continue;
                    }
    
                    if((choice < (totalAccs + 2)) && choice > 0) {

                        if(BankAdminScreens.canceledOnes.size() == 0) break;
                        for(int i = 0; i < BankAdminScreens.canceledOnes.size(); i++) {

                            if(choice != BankAdminScreens.canceledOnes.get(i)) arrIndex = false;
                            else {
                                arrIndex = true;
                                break;
                            }
                        }

                        if(arrIndex == true) System.out.println("That account was already canceled. Please try a different number");
                    }
                    else System.out.println("Please choose from the above numbers");
    
                } catch(InputMismatchException e) {
                    System.out.println("Please choose from the above numbers");
                    sc.nextLine();
                }
            } while(arrIndex);

            if(choice == (totalAccs + 1)) return;
            else {
                try(Connection conn = ConnectionUtil.getConnection()) {

                    choice = choice - 1;
                    BankAccount bA = myAccounts.get(choice);

                    PreparedStatement ps = null;
                    String sql;

                    sql = "DELETE FROM CUSTOMERBANKCONN WHERE B_ID = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, bA.getBankID());
                    ps.execute();
          
                    sql = "DELETE FROM BANKACCOUNTS WHERE B_ID = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, bA.getBankID());
                    ps.execute();

                    myAccounts.set(myAccounts.indexOf(bA), null);

                    System.out.println("\nCancel complete! Account " + bA.getBankID() + " has been removed");
        
                    ps.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Iterator<BankAccount> accIt = myAccounts.iterator();
        while(accIt.hasNext()) {
            if(accIt.next() == null) accIt.remove();
        }
    }

    //Lets the admin go through each pending account one by one and make a choice
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
                    System.out.print("\nWould you like to approve or deny account " + bA.getBankID() + "\n" + 
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

    //Load up all created bank accounts into the admin's account arraylist
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

    //Writes all accounts to a file and empties the admin's arraylist
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

    public String getBankAdminID() {
        return bankAdminID;
    }

    public String applyForAcc() {
        return null;
    }
}