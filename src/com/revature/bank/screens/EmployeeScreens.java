package com.revature.bank.screens;

import com.revature.bank.people.Employee;
import com.revature.bank.dbs.FileIO;
import java.io.*;
import java.util.*;

public class EmployeeScreens {
    public static void EmployeeMainMenu(Employee emp) {
        Scanner sc = new Scanner(System.in);
        int opt;
        int type;
        System.out.println("\nLogin successful! Hello " + emp.toString() + "!");

        do{
            System.out.println("Would you like to: \n" +
                            "1. View customer information \n" +
                            "2. Approve / Deny accounts \n" + 
                            "3. Logout");

            //Check whether their choice is either 1, 2, or 3
            do {
                try{
                    try{
                        opt = Integer.parseInt(sc.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.println("\nYour chosen number must be either 1, 2, or 3");
                        continue;
                    }
    
                    if(opt < 4 && opt > 0) break;
                    else System.out.println("Your chosen number must be either 1, 2, or 3");
    
                } catch(InputMismatchException e) {
                    System.out.println("You must choose either 1, 2, or 3 as your options");
                    sc.nextLine();
                }
            } while( true );


            //To look at all customer accounts and view their info
            if(opt == 1) emp.findAllCustomerAccs();
            else if(opt == 2) emp.approveOrDeny();   
            else return;

        } while (true);           
    }
}