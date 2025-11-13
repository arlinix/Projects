package Bank;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private int id;
    private List<Transaction> transactions = new ArrayList<>();

    public Account(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // 💸 Send money from this account to another
    public void sendMoneyToAccount(Account accountTo, double moneyAmount) {
        // Transaction for sender (this account)
        Transaction sendTx = new Transaction(this, accountTo, moneyAmount,
                StandardAccountOperations.MONEY_TRANSFER_SEND);
        transactions.add(sendTx);

        // Transaction for receiver
        Transaction receiveTx = new Transaction(this, accountTo, moneyAmount,
                StandardAccountOperations.MONEY_TRANSFER_RECEIVE);
        accountTo.transactions.add(receiveTx);
    }

    // 💵 Withdraw money
    public void withdrawMoney(double moneyAmount) {
        Transaction withdrawTx = new Transaction(this, null, moneyAmount,
                StandardAccountOperations.WITHDRAW);
        transactions.add(withdrawTx);
    }

    // 📜 Get copy of transactions
    public Transaction[] getTransactions() {
        return transactions.toArray(new Transaction[0]);
    }

    // 🧩 Nested class Transaction
    public class Transaction {
        private Account accountFrom;
        private Account accountTo;
        private double moneyAmount;
        private StandardAccountOperations operation;

        public Transaction(Account accountFrom, Account accountTo,
                           double moneyAmount, StandardAccountOperations operation) {
            this.accountFrom = accountFrom;
            this.accountTo = accountTo;
            this.moneyAmount = moneyAmount;
            this.operation = operation;
        }

        @Override
        public String toString() {
            return String.format("[%s] From: %s To: %s Amount: %.2f",
                    operation,
                    (accountFrom != null ? "Acc#" + accountFrom.getId() : "N/A"),
                    (accountTo != null ? "Acc#" + accountTo.getId() : "N/A"),
                    moneyAmount);
        }
    }
}
