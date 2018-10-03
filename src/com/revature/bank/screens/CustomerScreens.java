package com.revature.bank.screens;

import com.revature.bank.people.Customer;
import com.revature.bank.accounts.BankAccount;
import com.revature.bank.dbs.FileIO;
import java.io.*;
import java.util.*;

public class CustomerScreens {

    public static void CustomerMainMenu(Customer c) {
        Scanner sc = new Scanner(System.in);
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
                        System.out.println("\nYour chosen number must be either 1, 2, or 3");
                        continue;
                    }
    
                    if(opt < 4 && opt > 0) break;
                    else System.out.println("\nYour chosen number must be either 1, 2, or 3");
    
                } catch(InputMismatchException e) {
                    System.out.println("\nYou must choose either 1, 2, or 3 as your options");
                    sc.nextLine();
                }
            } while( true );

            //To display all current open accounts and make account changes
            if(opt == 1) CustomerScreens.CustomerAccountScreen(c);
            //To apply for a joint or single account
            else if(opt == 2) {
                System.out.print("\nWould you like to open a single or joint account? \n" +
                            "1. Single \n" +
                            "2. Joint \n" + 
                            "Choice: ");

                //if single account then currentUser.applyForAcc();
                do {
                    try{
                        try{
                            type = Integer.parseInt(sc.nextLine());
                        } catch(NumberFormatException e) {
                            System.out.println("\nYour chosen number must be either 1 or 2");
                            continue;
                        }
        
                        if(type < 3 && type > 0) break;

                        else System.out.println("\nYour chosen number must be either 1 or 2");
        
                    } catch(InputMismatchException e) {
                        System.out.println("\nYou must choose either 1 or 2 as your options");
                        sc.nextLine();
                    }
                } while( true );
            
                if(type == 1) {
                    String accountID = c.applyForAcc();
                    System.out.println("\nYou have created a new account! Its ID is " + accountID);
                    continue;
                }
                else {








                }
            }
            else {
                //logout and write bank account arraylist to a file
                c.writeAccounts();
                break;
            }
        } while(true);
    }

    public static void CustomerAccountScreen(Customer c) {
        System.out.print("\n");
        ArrayList<Integer> approvedIndex =  c.displayMyAccs();
        int totalAccs = approvedIndex.size();
        int choice;
        int choiceTransfer;
        double amt;
        BankAccount currentAccount = null;
        Scanner sc = new Scanner(System.in);

        if(totalAccs != 0) {
            //Ask the user here if they wanna look at an account or go back
            System.out.print("\nWhich approved account would you like to access? Or press " + (totalAccs + 1) + " to go back \n" +
                    "Choice: ");
            do {
                try{
                    try{
                        choice = Integer.parseInt(sc.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.println("\nPlease choose from the above numbers");
                        continue;
                    }
    
                    if((choice < (totalAccs + 2)) && choice > 0) break;
                    else System.out.println("\nPlease choose from the above numbers");
    
                } catch(InputMismatchException e) {
                    System.out.println("\nPlease choose from the above numbers");
                    sc.nextLine();
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
                            System.out.println("\nPlease choose from the above numbers");
                            continue;
                        }
        
                        if(moneyMoves < 5 && moneyMoves > 0) break;
                        else System.out.println("\nPlease choose from the above numbers");
        
                    } catch(InputMismatchException e) {
                        System.out.println("\nPlease choose from the above numbers");
                        sc.nextLine();
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
                                    System.out.println("\nPlease select a number");
                                    continue;
                                }
                
                                if(amt > 0) break;
                                else System.out.println("\nPlease select a number");
                                
                            } catch(InputMismatchException e) {
                                System.out.println("\nPlease select a number");
                                sc.nextLine();
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
                                    System.out.println("\nPlease select a number");
                                    continue;
                                }
                
                                if(amt > 0) break;
                                else System.out.println("Please select a number");
                
                            } catch(InputMismatchException e) {
                                System.out.println("Please select a number");
                                sc.nextLine();
                            }
                        } while( true );
                        currentAccount.withdraw(amt);
                    break;

                    case 3:
                        //Transfer money
                        if(totalAccs > 1) {
                            System.out.println("\nWhich account would you like to transfer? Or press " + (totalAccs + 1) + " to go back");
                            do {
                                c.displayMyAccs();
                                try{
                                    try{
                                        choiceTransfer = Integer.parseInt(sc.nextLine());
                                    } catch(NumberFormatException e) {
                                        System.out.println("\nPlease choose from the above numbers");
                                        continue;
                                    }
                    
                                    if((choiceTransfer < (totalAccs + 2)) && choiceTransfer > 0 && choice != choiceTransfer) break;
                                    else System.out.println("\nPlease choose from the above numbers or a different account");
                    
                                } catch(InputMismatchException e) {
                                    System.out.println("\nPlease choose from the above numbers");
                                    sc.nextLine();
                                }
                            } while( true );

                            System.out.println("\nHow much would you like to transfer \n" + 
                                    "Amount: ");
                            do {
                                try{
                                    try{
                                        amt = Double.parseDouble(sc.nextLine());
                                    } catch(NumberFormatException e) {
                                        System.out.println("\nPlease select a number");
                                        continue;
                                    }

                                    if(amt > 0) break;
                                    else System.out.println("\nPlease select a number");

                                } catch(InputMismatchException e) {
                                    System.out.println("\nPlease select a number");
                                    sc.nextLine();
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