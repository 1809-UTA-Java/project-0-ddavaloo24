package com.revature.bank.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.revature.bank.logging.*;
import com.revature.bank.people.BankAdmin;
import com.revature.bank.util.*;

public class LoginTest {
    
    @Test
    public void nameCheckerLengthTest() {

        String tooShort = "a";
        boolean checker = LoginValidator.nameChecker(tooShort);
        boolean actual = true;

        assertEquals(checker, actual);
    }

    @Test
    public void nameCheckerContentTest() {

        String wrongInput = "He llo";
        boolean checker = LoginValidator.nameChecker(wrongInput);
        boolean actual = false;

        assertEquals(checker, actual);
    }

    @Test
    public void usernameUniqueTest() {
        
        String username = "ALLJOJOOJJH";
        boolean checker = LoginValidator.usernameChecker(username);
        boolean actual = false;

        assertEquals(checker, actual);
    }

    public void insertTest() {

        String username = "tester123";
        String password = "tester321";
        String firstName = "theTest";
        String lastName = "THETEST";
        boolean actual = true;

        BankAdmin b = new BankAdmin(username, password, firstName, lastName);
        SuperDao.insertAdmin(b.getBankAdminID(), username, password, firstName, lastName);

        PreparedStatement ps = null;

        try(Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM ADMINS WHERE A_ID = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, b.getBankAdminID());
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                if(rs.getString("A_ID").equals(b.getBankAdminID())) {
                    if(rs.getString("A_FNAME").equals(firstName)) {
                        if(rs.getString("A_LNAME").equals(lastName)) {
                            if(rs.getString("A_USERNAME").equals(username)) {
                                if(rs.getString("A_PASSWORD").equals(password)) {
                                    assertEquals(actual, true);
                                }
                            }
                        }

                    }
                }
            }

            assertEquals(actual, false);

            ps.close();
            rs.close();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}