package com.revature.bank.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import com.revature.bank.people.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SuperDao {

    public static void insertCustomer(String accID, String username, String password, String firstName, String lastName) {
        PreparedStatement ps = null;

        try(Connection conn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO CUSTOMERS VALUES(?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, accID);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, username);
            ps.setString(5, password);

            ps.execute();
            ps.close();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void insertEmployee(String accID, String username, String password, String firstName, String lastName) {
        PreparedStatement ps = null;

        try(Connection conn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO EMPLOYEES VALUES(?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, accID);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, username);
            ps.setString(5, password);

            ps.execute();
            ps.close();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void insertAdmin(String accID, String username, String password, String firstName, String lastName) {
        PreparedStatement ps = null;

        try(Connection conn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO ADMINS VALUES(?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, accID);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, username);
            ps.setString(5, password);

            ps.execute();
            ps.close();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkUsernameUnique(String username) {
        PreparedStatement ps = null;
        
        try(Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT C_USERNAME FROM customers UNION SELECT E_USERNAME FROM employees UNION SELECT A_USERNAME FROM admins";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String c_username = rs.getString("C_USERNAME");

                if(username.equals(c_username)) {
                    System.out.println("Your username already exists. Please try a new one.");
                    return true;
                }
        
            }

            rs.close();
            ps.close();

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean checkLogin(String username, String password) {
        PreparedStatement ps = null;

        try(Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT C_USERNAME, C_PASSWORD FROM customers " + 
                "UNION " + 
                "SELECT E_USERNAME, E_PASSWORD FROM employees " + 
                "UNION " +  
                "SELECT A_USERNAME, A_PASSWORD FROM admins";

            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String queryUsername = rs.getString("C_USERNAME");
                String queryPassword = rs.getString("C_PASSWORD");

                if(queryUsername.equals(username) && queryPassword.equals(password)) return true;
            }

            rs.close();
            ps.close();

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Customer retrieveCustomerAccount(String username, String password) {
        PreparedStatement ps = null;

        try(Connection conn = ConnectionUtil.getConnection()) {
            
            String sql = "SELECT * FROM CUSTOMERS WHERE c_username=? AND c_password=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                return new Customer(
                    rs.getString("C_USERNAME"), 
                    rs.getString("C_PASSWORD"), 
                    rs.getString("C_FNAME"),
                    rs.getString("C_LNAME"),
                    rs.getString("C_ID"));
            }
                       
            rs.close();
            ps.close();

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Employee retrieveEmployeeAccount(String username, String password) {
        PreparedStatement ps = null;

        try(Connection conn = ConnectionUtil.getConnection()) {
            
            String sql = "SELECT * FROM EMPLOYEES WHERE c_username=? AND c_password=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                return new Employee(
                    rs.getString("E_USERNAME"), 
                    rs.getString("E_PASSWORD"), 
                    rs.getString("E_FNAME"),
                    rs.getString("E_LNAME"),
                    rs.getString("E_ID"));
            }
           
            rs.close();
            ps.close();

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static BankAdmin retrieveAdminAccount(String username, String password) {
        PreparedStatement ps = null;

        try(Connection conn = ConnectionUtil.getConnection()) {
            
            String sql = "SELECT * FROM ADMINS WHERE c_username=? AND c_password=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                return new BankAdmin(
                    rs.getString("A_USERNAME"), 
                    rs.getString("A_PASSWORD"), 
                    rs.getString("A_FNAME"),
                    rs.getString("A_LNAME"),
                    rs.getString("A_ID"));
            }

            rs.close();
            ps.close();

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean jointAccountCheck(String CID) {
        PreparedStatement ps = null;

        try(Connection conn = ConnectionUtil.getConnection()) {
            
            String sql = "SELECT C_ID FROM CUSTOMERS";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                if(rs.getString("C_ID").equals(CID)) return true;
            }

            rs.close();
            ps.close();

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        System.out.println("That is not a valid account");
        return false;
    }


    
}