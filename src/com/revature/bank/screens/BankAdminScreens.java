package com.revature.bank.screens;

import com.revature.bank.people.BankAdmin;
import com.revature.bank.screens.*;
import com.revature.bank.accounts.BankAccount;
import com.revature.bank.dbs.FileIO;
import java.io.*;
import java.util.*;

/**
 * Create the default main menu for the bank admin as well as the account 
 * manipulation interface for all customer accounts
 */
public class BankAdminScreens {

    public static ArrayList<Integer> canceledOnes = new ArrayList<>();

    public static boolean BankAdminMainMenu(BankAdmin bAd, Scanner sc) {

        int opt;
        System.out.println("\nLogin successful! Hello " + bAd.toString() + "!");

        do{
            System.out.println("\n----------------------------------------------------");
            System.out.print("Would you like to: \n" +
                            "1. Display all bank accounts \n" +
                            "2. Approve / Deny accounts \n" + 
                            "3. Cancel Accounts \n" + 
                            "4. Logout \n" + 
                            "Choice: ");

            //Check whether their choice is either 1, 2, 3, or 4
            do {
                try{
                    try{
                        opt = Integer.parseInt(sc.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.print("\nYour chosen number must be either 1, 2, 3, or 4 \n" +
                                "Choice: ");
                        continue;
                    }
    
                    if(opt < 5 && opt > 0) break;
                    else {
                        System.out.print("\nYour chosen number must be either 1, 2, 3, or 4 \n" +
                                "Choice: ");
                    }
    
                } catch(InputMismatchException e) {
                    System.out.print("\nYour chosen number must be either 1, 2, 3, or 4 \n" +
                                "Choice: ");
                }
            } while( true );

            //To view all accounts and and move money around
            if(opt == 1) BankAdminScreens.BankAdminAccountScreen(bAd, sc);
            else if(opt == 2) bAd.approveOrDeny(sc);
            else if(opt == 3) bAd.cancelBankAccount(sc);
            else {
                //logout and write bank account arraylist to a file
                bAd.writeAccounts();
                System.out.println("\nYou have logged out!");
                return true;
            }

        } while(true);
    }

    //Page admin sees when it is trying to move money around
    public static void BankAdminAccountScreen(BankAdmin bAd, Scanner sc) {
        ArrayList<BankAccount> approvedIndex =  bAd.displayMyAccs();
        int totalAccs = approvedIndex.size();
        int choice = 0;
        double amt = 0;
        double amtNotRounded = 0;
        int choiceTransfer = 0;
        boolean loopCheck = true;
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
            } while( loopCheck );

            if(choice != (totalAccs + 1)) {

                currentAccount =  approvedIndex.get(choice - 1);
                System.out.println("\n----------------------------------------------------");
                System.out.print("What would you like to do with the account? \n" +
                            "1. Deposit money \n" + 
                            "2. Withdraw money \n" +
                            "3. Transfer money between the accounts \n" +
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
                                    amtNotRounded = Double.parseDouble(sc.nextLine());
                                    amt = (double) Math.round(amtNotRounded*100)/100;
                                } catch(NumberFormatException e) {
                                    System.out.print("\nPlease select a positive, valid number \n" +
                                            "Amount: ");
                                    continue;
                                }
                
                                if(amt > 0) break;
                                else {
                                    System.out.print("\nPlease select a positive, valid number \n" +
                                            "Amount: ");
                                }
                
                            } catch(InputMismatchException e) {
                                System.out.print("\nPlease select a positive, valid number \n" +
                                            "Amount: ");
                            }
                        } while( true );
                        currentAccount.deposit(amt);
                        bAd.writeAccounts();

                    break;
                    case 2:
                        //Parse and withdraw
                        System.out.print("\nHow much would you like to withdraw \n" + 
                                "Amount: ");
                        do {
                            try{
                                try{
                                    amtNotRounded = Double.parseDouble(sc.nextLine());
                                    amt = (double) Math.round(amtNotRounded*100)/100;

                                } catch(NumberFormatException e) {
                                    System.out.print("\nPlease select a positive, valid number \n" +
                                            "Amount: ");
                                    continue;
                                }
                
                                if(amt > 0) break;
                                else {
                                    System.out.print("\nPlease select a positive, valid number \n" +
                                            "Amount: ");
                                }
                
                            } catch(InputMismatchException e) {
                                System.out.print("\nPlease select a positive, valid number \n" +
                                            "Amount: ");
                            }
                        } while( true );

                        currentAccount.withdraw(amt);
                        bAd.writeAccounts();
                    break;
                    case 3:
                        //Transfer money
                        if(totalAccs > 1) {
                            System.out.println(" ");
                            bAd.displayMyAccs();
                            System.out.print("\nWhich account would you like to transfer money to? Or press " + 
                                (totalAccs + 1) + " to go back \n" +
                                "Choice: ");
                            do {
                                try{
                                    try{
                                        choiceTransfer = Integer.parseInt(sc.nextLine());
                                    } catch(NumberFormatException e) {
                                        System.out.print("\nPlease choose from the above numbers \n" +
                                                "Choice: ");
                                    }
                    
                                    if((choiceTransfer < (totalAccs + 2)) && choiceTransfer > 0 && choice != choiceTransfer) break;
                                    else {
                                        System.out.print("\nPlease choose from the above accounts or a different destination \n" +
                                                "Choice: ");
                                    }
                    
                                } catch(InputMismatchException e) {
                                    System.out.print("\nPlease choose from the above numbers \n" +
                                                "Choice: ");
                                }
                            } while( true );

                            if(choiceTransfer == totalAccs + 1) return;

                            System.out.print("\nHow much would you like to transfer \n" + 
                                    "Amount: ");
                            do {
                                try{
                                    try{
                                        amtNotRounded = Double.parseDouble(sc.nextLine());
                                        amt = (double) Math.round(amtNotRounded*100)/100;
                                    } catch(NumberFormatException e) {
                                        System.out.print("\nPlease select a positive, valid number \n" +
                                            "Amount: ");
                                        continue;
                                    }
                                    
                                    if(amt > 0) break;
                                    else {
                                        System.out.print("\nPlease select a positive, valid number \n" +
                                            "Amount: ");
                                    }

                                } catch(InputMismatchException e) {
                                    System.out.print("\nPlease select a positive, valid number \n" +
                                            "Amount: ");
                                }
                            } while( true );

                            BankAccount secondAcc = bAd.getAccount(choiceTransfer - 1);
                            currentAccount.transfer(amt, secondAcc);
                            bAd.writeAccounts();
                        }
                        else {
                            System.out.println("\nYou do not have enough accounts!");
                        }
                    break;
                    default:
                }
            }
            else return;
        }
        else return;
    }
}