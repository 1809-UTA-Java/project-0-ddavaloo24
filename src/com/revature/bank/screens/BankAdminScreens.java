package com.revature.bank.screens;

import com.revature.bank.people.BankAdmin;
import com.revature.bank.screens.*;
import com.revature.bank.accounts.BankAccount;
import com.revature.bank.dbs.FileIO;
import java.io.*;
import java.util.*;


public class BankAdminScreens {
    public static ArrayList<Integer> canceledOnes = new ArrayList<>();

    public static void BankAdminMainMenu(BankAdmin bAd) {
        Scanner sc = new Scanner(System.in);
        int opt;
        int type;
        

        System.out.println("\nLogin successful! Hello " + bAd.toString() + "!");

        do{
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
                        System.out.println("\nYour chosen number must be either 1, 2, 3, or 4");
                        continue;
                    }
    
                    if(opt < 5 && opt > 0) break;
                    else System.out.println("\nYour chosen number must be either 1, 2, 3, or 4");
    
                } catch(InputMismatchException e) {
                    System.out.println("\nYou must choose either 1, 2, 3, or 4 as your options");
                    sc.nextLine();
                }
            } while( true );

            //To view all accounts and and move money around
            if(opt == 1) BankAdminScreens.BankAdminAccountScreen(bAd);
            else if(opt == 2) bAd.approveOrDeny();
            else if(opt == 3) bAd.cancelBankAccount();
            else {
                //logout and write bank account arraylist to a file
                bAd.writeAccounts();
                break;
            }

        } while(true);
    }

    public static void BankAdminAccountScreen(BankAdmin bAd) {
        System.out.print("\n");
        ArrayList<Integer> approvedIndex =  bAd.displayMyAccs();
        int totalAccs = approvedIndex.size();
        int choice = 0;
        double amt;
        int choiceTransfer;
        boolean loopCheck = true;
        BankAccount currentAccount = null;
        Scanner sc = new Scanner(System.in);

        if(totalAccs != 0) {
            //Ask the user here if they wanna look at an account or go back
            System.out.println("\nWhich account would you like to access? Or press " + (totalAccs + 1) + " to go back");
            do {
                try{
                    try{
                        choice = Integer.parseInt(sc.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.println("\nPlease choose from the above numbers");
                        continue;
                    }
    
                    if((choice < (totalAccs + 2)) && choice > 0) break;
                    else System.out.println("Please choose from the above numbers");
                } catch(InputMismatchException e) {
                    System.out.println("Please choose from the above numbers");
                    sc.nextLine();
                }
            } while( loopCheck );

            if(choice != (totalAccs + 1)) {
                int arrIndex = approvedIndex.get(choice - 1);

                currentAccount = bAd.getAccount(arrIndex);
                System.out.println("What would you like to do with the accounts? \n" +
                            "1. Deposit money \n" + 
                            "2. Withdraw money \n" +
                            "3. Transfer money between the accounts \n" +
                            "4. Go to home screen");

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
                        else System.out.println("Please choose from the above numbers");
        
                    } catch(InputMismatchException e) {
                        System.out.println("Please choose from the above numbers");
                        sc.nextLine();
                    }
                } while( true );

                switch(moneyMoves) {
                    case 1:
                        //Parse money amount and deposit
                        System.out.print("How much would you like to deposit \n" + 
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
                        currentAccount.deposit(amt);
                    break;
                    case 2:
                        //Parse and withdraw
                        System.out.print("How much would you like to withdraw \n" + 
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
                            System.out.println("\nWhich account would you like to transfer money from? Or press " + 
                                (totalAccs + 1) + " to go back");
                            do {
                                bAd.displayMyAccs();
                                try{
                                    try{
                                        choiceTransfer = Integer.parseInt(sc.nextLine());
                                    } catch(NumberFormatException e) {
                                        System.out.println("\nPlease choose from the above numbers");
                                        continue;
                                    }
                    
                                    if((choiceTransfer < (totalAccs + 2)) && choiceTransfer > 0 && choice != choiceTransfer) break;
                                    else System.out.println("Please choose from the above numbers or a different account");
                    
                                } catch(InputMismatchException e) {
                                    System.out.println("Please choose from the above numbers");
                                    sc.nextLine();
                                }
                            } while( true );

                            System.out.println("How much would you like to transfer \n" + 
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

                            BankAccount secondAcc = bAd.getAccount(choiceTransfer - 1);
                            currentAccount.transfer(amt, secondAcc);
                        }
                        else {
                            System.out.println("You do not have enough accounts!");
                        }
                    break;
                    default:
                }
            }
        }
        else return;
    }
}