package xyz;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome the XYZ Bank Wallet");
        while (true){
            System.out.println("1. Create Account" + "\n" +
                    "2. Login" + "\n" +
                    "3. Exit");
            int choice = Integer.parseInt(sc.nextLine());
            Connectivity c = new Connectivity();
            switch (choice) {
                case 1:
                    String name, userId, password;
                    Integer balance;
                    System.out.println("Account Creation");
                    System.out.println("Enter your name");
                    name = sc.nextLine();
                    //Automatically creating userid from name
                    userId = name.replaceAll("\\s", "") + "@xyz";
                    //to check if it's already present in db or not
                    if (c.checkuserId(userId)) {
                        System.out.println("Your user Id will be: " + userId);
                    } else {
                        System.out.println("Enter UserId: ");
                        userId = sc.nextLine();
                        System.out.println("Your user Id will be: " + userId);
                    }

                    System.out.println("Enter your password");
                    password = sc.nextLine();
                    if (c.createAccount(name, userId, password, 0)) {
                        System.out.println("Account Created Successfully");
                    } else {
                        System.out.println("Account Not Created, try once again...");
                    }
                    break;
                case 2:
                    System.out.println("Account Login");
                    System.out.println("Enter your userId");
                    userId = sc.nextLine();
                    System.out.println("Enter your password");
                    password = sc.nextLine();

                    //To check for credentials
                    if (c.verifyUser(userId, password)) {
                        System.out.println("Login Successful");
                        while (true) {
                            System.out.println("1. Add amount to your account" + "\n" +
                                    "2. Withdraw from your account" + "\n" +
                                    "3. Transfer to another account (with in XYZ Bank Only)" + "\n" +
                                    "4. Check Account Balance" + "\n" +
                                    "5. Show Transaction history" + "\n" +
                                    "6. Exit" + "\n");
                            int action = Integer.parseInt(sc.nextLine());
                            switch (action) {
                                case 1:
                                    System.out.println("Add amount to your account");
                                    System.out.println("Enter the amount: ");
                                    int amount = Integer.parseInt(sc.nextLine());
                                    c.addAmountToUser(amount, userId);
                                    c.showAccountBalance(userId, password);
                                    break;
                                case 2:
                                    System.out.println("Withdraw from your account");
                                    System.out.println("Enter the amount: ");
                                    amount = Integer.parseInt(sc.nextLine());
                                    c.removeAmountFromUser(amount, userId, password);
                                    c.showAccountBalance(userId, password);
                                    break;
                                case 3:
                                    System.out.println("Transfer to another account (with in XYZ Bank Only)");
                                    System.out.println("Enter the userId ");
                                    String userId1 = sc.nextLine();
                                    System.out.println("Enter the amount to be transfered: ");
                                    amount = Integer.parseInt(sc.nextLine());
                                    if ((c.userExistsOrNot(userId1)) && (userId != userId1)) {
                                        c.transferAmount(userId, userId1, password, amount);
                                        c.showAccountBalance(userId, password);
                                    } else {
                                        System.out.println("UserId Doesn't exists");
                                    }
                                    break;
                                case 4:
                                    System.out.println("Check Account Balance");
                                    c.showAccountBalance(userId, password);
                                    break;
                                case 5:
                                    System.out.println("Show Transaction history");
                                    c.showTransactionHistory(userId);
                                    break;
                                case 6:
                                    System.exit(0);
                            }
                        }
                    }
                    else {
                        System.out.println("Invalid Credentials");
                    }
                    break;
                case 3:
                    System.exit(0);
            }
        }
    }
}
