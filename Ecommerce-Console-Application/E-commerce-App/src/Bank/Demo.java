package Bank;

public class Demo {
    public static void main(String[] args) {
        Account acc1 = new Account(101);
        Account acc2 = new Account(102);

        acc1.sendMoneyToAccount(acc2, 500.0);
        acc1.withdrawMoney(200.0);
        acc2.sendMoneyToAccount(acc1, 150.0);

        System.out.println("💰 Transactions for Account 101:");
        for (Account.Transaction t : acc1.getTransactions()) {
            System.out.println(t);
        }

        System.out.println("\n💰 Transactions for Account 102:");
        for (Account.Transaction t : acc2.getTransactions()) {
            System.out.println(t);
        }
    }
}
