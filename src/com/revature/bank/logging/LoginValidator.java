package com.revature.bank.logging;

import com.revature.bank.dbs.FileIO;

class LoginValidator {

    //Method to check whether the first or last name in the account creation stage is valid
    public static boolean nameChecker(String name) {

        char ch;

        //Check if either last or first name meets the size req of 2-20 chars
        if(name.length() < 2 || name.length() > 20) {
            System.out.println("The name does not meet the size requirements");
            return true;
        }

        //Checks for any special characters
        for(int i = 0; i < name.length(); i++) {
            ch = name.charAt(i);
            if(Character.isLetter(ch) || Character.isWhitespace(ch)) {}
            else {
                System.out.println("The name cannot include any special characters");

                //Must return true to keep the outer loop in Login.java running
                return true;
            }
        }

        return false;
    }

    //Method to check if the username is valid for account creation
    public static boolean usernameChecker(String creds) {
        char uscore = '_';
        char ch;

        //Check if username meets the size req of 6-12 chars
        if(creds.length() < 6 || creds.length() > 12) {
            System.out.println("Your username must be between 8-12 characters");
            return true;
        }

        for(int i = 0; i < creds.length(); i++) {

            ch = creds.charAt(i);
            if(ch == uscore || Character.isWhitespace(ch)) {
                System.out.println("Your choice cannot include underscores or spaces");
                return true;
            }
        }

        //Check if the username is unique
        if( FileIO.lookupLogin("AllCustomers", creds) ) {
            if( FileIO.lookupLogin("AllEmployees", creds) ) {
                if( FileIO.lookupLogin("AllAdmins", creds) ) {
                    System.out.println("Your username already exists. Please try a new one.");
                    return true;
                }
            }
        }

        return false;
    }

    //Checks password validity
    public static boolean passwordChecker(String creds) {
        char uscore = '_';
        char ch;

        //Check if pw meets the size req of 8-12 chars
        if(creds.length() < 8 || creds.length() > 12) {
            System.out.println("You choice must be between 8-12 characters");
            return true;
        }

        for(int i = 0; i < creds.length(); i++) {

            ch = creds.charAt(i);
            if(ch == uscore || Character.isWhitespace(ch)) {
                System.out.println("Your choice cannot include underscores or spaces");
                return true;
            }
        }

        return false;
    }


}