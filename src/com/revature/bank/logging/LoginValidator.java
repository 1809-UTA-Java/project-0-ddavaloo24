package com.revature.bank.logging;

class LoginValidator {

    public static boolean nameChecker(String name) {

        char ch;

        //Check if either last or first name meets the size req of 2-20 chars
        if(name.length() < 2 && name.length() > 20) {
            System.out.println("The name does not meet the size requirements");
            return false;
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

}