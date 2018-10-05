package com.revature.bank.screens;

import com.revature.bank.people.Employee;
import com.revature.bank.dbs.FileIO;
import java.io.*;
import java.util.*;

/**
 * Create the screen the employee sees with his options
 */
public class EmployeeScreens {
    public static boolean EmployeeMainMenu(Employee emp, Scanner sc) {

        int opt;
        System.out.println("\nLogin successful! Hello " + emp.toString() + "!");

        do{
            System.out.print("\nWould you like to: \n" +
                            "1. View customer information \n" +
                            "2. Approve / Deny accounts \n" + 
                            "3. Logout \n" + 
                            "Choice: ");

            //Check whether their choice is either 1, 2, or 3
            do {
                try{
                    try{
                        opt = Integer.parseInt(sc.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.print("\nYour chosen number must be either 1, 2, or 3 \n" +
                                "Choice: ");
                        continue;
                    }
    
                    if(opt < 4 && opt > 0) break;
                    else {
                        System.out.print("\nYour chosen number must be either 1, 2, or 3 \n" +
                                "Choice: ");
                    }
    
                } catch(InputMismatchException e) {
                    System.out.print("\nYour chosen number must be either 1, 2, or 3 \n" +
                                "Choice: ");
                }
            } while( true );

            //To look at all customer accounts and view their info
            if(opt == 1) emp.findAllCustomerAccs(sc);
            else if(opt == 2) emp.approveOrDeny(sc);   
            else return true;

        } while (true);           
    }
}