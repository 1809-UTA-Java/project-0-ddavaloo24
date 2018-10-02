package com.revature.bank;
   
import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.revature.bank.accounts.*;
import com.revature.bank.dbs.FileIO;
import com.revature.bank.people.*;
import com.revature.bank.logging.*;


public class Bank {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Login login = new Login();

        Customer currentCustomer = null;
        Employee currentEmployee = null;
        BankAdmin currentBankAdmin = null;
        BankAccount currentAccount = null;

        //Used to check if the correct input was given when asking for a yes or no on the main screen
        boolean check;
        String answer;

        //Creates a default login screen and makes sure user enters yes or no
        do {

            System.out.println("Welcome to the Bank of Darius! Do you have an account with us? Yes or no");
            answer = sc.nextLine().toLowerCase().trim();

            if(answer.equals("no") || answer.equals("yes")) break;
            else {
                System.out.println("Sorry! I didn't get that! Please try again");
            } 

        } while(true);

        do {
            
            if(answer.equals("no")) {
                //If the user does not have an account, make one for them
                login.createAccount();
                check = false;
            }
            else if(answer.equals("yes")) {
                //If the user already has an account
                check = false;
            }
            else {
                //If the user enters anything other than a yes or no
                System.out.println("Please write yes or no");
                answer = sc.nextLine().toLowerCase();
                check = true;   
            }
        } while(check);

        do {
            int type;
            do {
                System.out.println("\nWhat kind of account would you like to login to? \n" +
                                "1. Customer \n" +
                                "2. Employee \n" +
                                "3. Admin");
                try{
                    try{
                        type = Integer.parseInt(sc.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.println("\nYour chosen number must be either 1, 2, or 3");
                        continue;
                    }

                    if(type < 4 && type > 0) break;
                    else {
                        System.out.println("Your chosen number must be either 1, 2, or 3");
                    }
                } catch(InputMismatchException e) {
                    System.out.println("You must choose either 1, 2, or 3 as your options");
                    sc.nextLine();
                }
            } while( true );

            //IF A CUSTOMER
            if(type == 1) {
                currentCustomer = login.loginCustomer();
            }
            //IF AN EMPLOYEE
            else if (type == 2) {
                currentEmployee = login.loginEmployee();
            }
            //IF AN ADMIN
            else {
                currentBankAdmin = login.loginBankAdmin();
            }

            if(currentCustomer == null && currentEmployee == null && currentBankAdmin == null) {
                System.out.println("Your credentials were not in the chosen database. \n");
            }
        } while(currentCustomer == null && currentEmployee == null && currentBankAdmin == null);
        

        if(type == 1) {
            //IF USER IS A CUSTOMER
            int opt;
            System.out.println("\nLogin successful! Hello " + currentCustomer.toString() + "!");

            do{
                System.out.println("Would you like to: \n" +
                                "1. Display my bank accounts \n" +
                                "2. Apply for a new account \n" + 
                                "3. Logout");

                do {
                    try{
                        try{
                            opt = Integer.parseInt(sc.nextLine());
                        } catch(NumberFormatException e) {
                            System.out.println("\nYour chosen number must be either 1, 2, or 3");
                            continue;
                        }
        
                        if(opt < 4 && opt > 0) {
                            break;
                        }
                        else {
                            System.out.println("Your chosen number must be either 1, 2, or 3");
                        }
        
                    } catch(InputMismatchException e) {
                        System.out.println("You must choose either 1, 2, or 3 as your options");
                        sc.nextLine();
                    }
                } while( true );



                //To display all current open accounts and make account changes
                if(opt == 1) {
                    System.out.print("\n");
                    int totalAccs = currentCustomer.displayMyAccs();
                    int choice;

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
            
                            if((choice < (totalAccs + 2)) && choice > 0) {
                                break;
                            }
                            else {
                                System.out.println("Please choose from the above numbers");
                            }
            
                        } catch(InputMismatchException e) {
                            System.out.println("Please choose from the above numbers");
                            sc.nextLine();
                        }
                    } while( true );

                    if(choice != (totalAccs + 1)) {
                        int arrIndex = choice - 1;
                        currentAccount = currentCustomer.getAccount(arrIndex);
                        System.out.println("What would you like to do with your account? \n" +
                                    "1. Deposit money \n" + 
                                    "2. Withdraw money \n" +
                                    "3. Transfer money between your accounts \n" +
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
                
                                if(moneyMoves < 5 && moneyMoves > 0) {
                                    break;
                                }
                                else {
                                    System.out.println("Please choose from the above numbers");
                                }
                
                            } catch(InputMismatchException e) {
                                System.out.println("Please choose from the above numbers");
                                sc.nextLine();
                            }
                        } while( true );

                        switch(moneyMoves) {
                            case 1:
                                //Parse money amount
                                currentAccount.deposit(amt);
                            break;
                            case 2:

                            break;

                            case 3:

                            break;

                            default:
                        }









                    }
                }
                //To apply for a joint or single account
                else if(opt == 2) {
                    System.out.println("\nWould you like to open a single or joint account? \n" +
                                "1. Single \n" +
                                "2. Joint");

                    //if single account then currentUser.applyForAcc();
                    do {
                        try{
                            try{
                                type = Integer.parseInt(sc.nextLine());
                            } catch(NumberFormatException e) {
                                System.out.println("\nYour chosen number must be either 1 or 2");
                                continue;
                            }
            
                            if(type < 3 && type > 0) {
                                break;
                            }
                            else {
                                System.out.println("Your chosen number must be either 1 or 2");
                            }
            
                        } catch(InputMismatchException e) {
                            System.out.println("You must choose either 1 or 2 as your options");
                            sc.nextLine();
                        }
                    } while( true );
                


                    if(type == 1) {
                        String accountID = currentCustomer.applyForAcc();
                        System.out.println("\nYou have created a new account! Its ID is " + accountID);
                        continue;
                    }
                    else {

                    }

                }
                else {
                    //logout


                    //write bank account arraylist to a file
                    currentCustomer.writeAccounts();
                    break;
                }

            } while(true);
        }
        else if(type == 2) {
            //IF USER IS AN EMPLOYEE

        }
        else {
            //IF USER IS AN ADMIN
        }

        
        











        














        sc.close();

    }
}