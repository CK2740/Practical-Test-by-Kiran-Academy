import java.util.Scanner;

/**
 * Kiran Academy - Task 03
 * Banking Console Application: Balance, Deposit & Withdrawal
 */
public class BankingApplication {

    // Reads a line and converts it to a number.
    // Returns -1 if the input is not a valid number (so it is treated as invalid).
    static double readAmount(Scanner sc) {
        String text = sc.nextLine().trim();
        try {
            double value = Double.parseDouble(text);
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                return -1;
            }
            return value;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double balance = 10000;   // fixed opening balance

        System.out.println("Welcome to Kiran Bank");
        System.out.println("Opening Balance: \u20B9" + balance);

        while (true) {
            System.out.println();
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                System.out.println("Current Balance: \u20B9" + balance);

            } else if (choice.equals("2")) {
                System.out.print("Enter deposit amount: ");
                double amount = readAmount(sc);

                if (amount <= 0) {
                    System.out.println("Invalid amount. Please enter an amount greater than \u20B90.");
                } else {
                    balance = balance + amount;
                    System.out.println("Deposit successful.");
                    System.out.println("Updated Balance: \u20B9" + balance);
                }

            } else if (choice.equals("3")) {
                System.out.print("Enter withdrawal amount: ");
                double amount = readAmount(sc);

                if (amount <= 0) {
                    System.out.println("Invalid amount. Please enter an amount greater than \u20B90.");
                } else if (amount > balance) {
                    System.out.println("Transaction failed: Insufficient balance.");
                    System.out.println("Current Balance: \u20B9" + balance);
                } else {
                    balance = balance - amount;
                    System.out.println("Withdrawal successful.");
                    System.out.println("Updated Balance: \u20B9" + balance);
                }

            } else if (choice.equals("4")) {
                System.out.println("Thank you for banking with us. Goodbye!");
                break;

            } else {
                System.out.println("Invalid choice. Please enter 1, 2, 3 or 4.");
            }
        }

        sc.close();
    }
}