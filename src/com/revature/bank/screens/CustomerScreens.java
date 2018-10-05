package com.revature.bank.screens;

import com.revature.bank.people.Customer;
import com.revature.bank.util.SuperDao;
import com.revature.bank.accounts.BankAccount;
import com.revature.bank.dbs.FileIO;
import java.io.*;
import java.util.*;

/**
 * Give the customer a main menu as well as an interface for 
 * working with their approved accounts.
 */
public class CustomerScreens {

    public static void CustomerMainMenu(Customer c, Scanner sc) {

        int opt;
        int type;
        System.out.println("\nLogin successful! Hello " + c.toString() + "!");

        do{
            System.out.print("Would you like to: \n" +
                            "1. Display my bank accounts \n" +
                            "2. Apply for a new account \n" + 
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

            //To display all current open accounts and make account changes
            if(opt == 1) CustomerScreens.CustomerAccountScreen(c, sc);
            //To apply for a joint or single account
            else if(opt == 2) {
                System.out.print("\nWould you like to open a single or joint account? \n" +
                            "1. Single \n" +
                            "2. Joint \n" + 
                            "Choice: ");

                do {
                    try{
                        try{
                            type = Integer.parseInt(sc.nextLine());
                        } catch(NumberFormatException e) {
                            System.out.print("\nYour chosen number must be either 1 or 2 \n" + 
                                    "Choice: ");
                            continue;
                        }
        
                        if(type < 3 && type > 0) break;
                        else {
                            System.out.print("\nYour chosen number must be either 1 or 2 \n" + 
                                    "Choice: ");
                        }
        
                    } catch(InputMismatchException e) {
                        System.out.print("\nYour chosen number must be either 1 or 2 \n" + 
                                    "Choice: ");
                    }
                } while( true );
            
                if(type == 1) {
                    String accountID = c.applyForAcc();
                    System.out.println("\nYou have created a new account! Its ID is " + accountID);
                    continue;
                }
                else {
                    System.out.print("\nPlease provide the customer id of the second applier exactly as it appears. \n" + 
                                "Choice: ");
                    
                    String secondPerson = sc.nextLine();
                    boolean valid = SuperDao.jointAccountCheck(secondPerson);

                    if(valid) {
                        String accountID = c.applyForJointAcc(secondPerson);
                        System.out.println("\nYou have created a new account! Its ID is " + accountID);
                        continue;
                    }
                }
            }
            else {
                //logout and write bank account arraylist to a file
                c.writeAccounts();
                break;
            }
        } while(true);
    }

    public static void CustomerAccountScreen(Customer c, Scanner sc) {
        
        System.out.print("\n");
        ArrayList<Integer> approvedIndex =  c.displayMyAccs();
        int totalAccs = approvedIndex.size();
        int choice;
        int choiceTransfer;
        double amt;
        BankAccount currentAccount = null;

        if(totalAccs != 0) {
            //Ask the user here if they wanna look at an account or go back
            System.out.print("\nWhich approved account would you like to access? Or press " + (totalAccs + 1) + " to go back \n" +
                    "Choice: ");
            do {
                try{
                    try{
                        choice = Integer.parseInt(sc.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.print("\nPlease choose from the above numbers \n" + 
                                "Choice: ");
                        continue;
                    }
    
                    if((choice < (totalAccs + 2)) && choice > 0) break;
                    else {
                        System.out.print("\nPlease choose from the above numbers \n" + 
                                "Choice: ");
                    }
    
                } catch(InputMismatchException e) {
                    System.out.print("\nPlease choose from the above numbers \n" + 
                                "Choice: ");
                }
            } while( true );

            if(choice != (totalAccs + 1)) {
                int arrIndex = approvedIndex.get(choice - 1);
                currentAccount = c.getAccount(arrIndex);
                System.out.print("\nWhat would you like to do with your account? \n" +
                            "1. Deposit money \n" + 
                            "2. Withdraw money \n" +
                            "3. Transfer money between your accounts \n" +
                            "4. Go to home screen \n" + 
                            "Choice: ");

                int moneyMoves;
                do {
                    try{
                        try{
                            moneyMoves = Integer.parseInt(sc.nextLine());
                        } catch(NumberFormatException e) {
                            System.out.print("\nPlease choose from the above numbers \n" + 
                                "Choice: ");
                            continue;
                        }
        
                        if(moneyMoves < 5 && moneyMoves > 0) break;
                        else {
                            System.out.print("\nPlease choose from the above numbers \n" + 
                                "Choice: ");
                        }
        
                    } catch(InputMismatchException e) {
                        System.out.print("\nPlease choose from the above numbers \n" + 
                        "Choice: ");
                    }
                } while( true );

                switch(moneyMoves) {
                    case 1:
                        //Parse money amount and deposit
                        System.out.print("\nHow much would you like to deposit \n" + 
                                "Amount: ");
                        do {
                            try{
                                try{
                                    amt = Double.parseDouble(sc.nextLine());
                                } catch(NumberFormatException e) {
                                    System.out.print("\nPlease select a number \n" + 
                                            "Amount: ");
                                    continue;
                                }
                
                                if(amt > 0) break;
                                else {
                                    System.out.print("\nPlease select a number \n" + 
                                            "Amount: ");
                                }
                                
                            } catch(InputMismatchException e) {
                                System.out.print("\nPlease select a number \n" + 
                                            "Amount: ");
                            }
                        } while( true );
                        currentAccount.deposit(amt);
                    break;
                    case 2:
                        //Parse and withdraw
                        System.out.print("\nHow much would you like to withdraw \n" + 
                                "Amount: ");
                        do {
                            try{
                                try{
                                    amt = Double.parseDouble(sc.nextLine());
                                } catch(NumberFormatException e) {
                                    System.out.print("\nPlease select a number \n" + 
                                            "Amount: ");
                                    continue;
                                }
                
                                if(amt > 0) break;
                                else {
                                    System.out.print("\nPlease select a number \n" + 
                                            "Amount: ");
                                }
                
                            } catch(InputMismatchException e) {
                                System.out.print("\nPlease select a number \n" + 
                                            "Amount: ");
                            }
                        } while( true );
                        currentAccount.withdraw(amt);
                    break;

                    case 3:
                        //Transfer money
                        if(totalAccs > 1) {
                            System.out.print("\nWhich account would you like to transfer? Or press " + (totalAccs + 1) + " to go back \n" + 
                                        "Choice: ");
                            do {
                                c.displayMyAccs();
                                try{
                                    try{
                                        choiceTransfer = Integer.parseInt(sc.nextLine());
                                    } catch(NumberFormatException e) {
                                        System.out.print("\nPlease choose from the above numbers \n" + 
                                                "Choice: ");
                                        continue;
                                    }
                    
                                    if((choiceTransfer < (totalAccs + 2)) && choiceTransfer > 0 && choice != choiceTransfer) break;
                                    else {
                                        System.out.print("\nPlease choose from the above numbers \n" + 
                                                "Choice: ");
                                    }
                    
                                } catch(InputMismatchException e) {
                                    System.out.print("\nPlease choose from the above numbers \n" + 
                                                "Choice: ");
                                }
                            } while( true );

                            System.out.print("\nHow much would you like to transfer \n" + 
                                    "Amount: ");
                            do {
                                try{
                                    try{
                                        amt = Double.parseDouble(sc.nextLine());
                                    } catch(NumberFormatException e) {
                                        System.out.print("\nPlease select a number \n" +
                                                "Amount: ");
                                        continue;
                                    }

                                    if(amt > 0) break;
                                    else {
                                        System.out.print("\nPlease select a number \n" +
                                                "Amount: ");
                                    }

                                } catch(InputMismatchException e) {
                                    System.out.print("\nPlease select a number \n" +
                                                "Amount: ");
                                }
                            } while( true );

                            BankAccount secondAcc = c.getAccount(choiceTransfer - 1);
                            currentAccount.transfer(amt, secondAcc);
                        }
                        else System.out.println("\nYou do not have enough accounts!");
                    break;
                    default:
                }
            }
        }
    }
}