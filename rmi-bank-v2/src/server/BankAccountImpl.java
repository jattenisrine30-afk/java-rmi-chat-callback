package server;

import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import interfaces.BankAccount;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class BankAccountImpl extends UnicastRemoteObject implements BankAccount {

    private final Map<String, Double> accounts = new HashMap<>();

    public BankAccountImpl() throws RemoteException {
        super();
        accounts.put("A001", 1000.0);
        accounts.put("A002", 500.0);
        accounts.put("A003", 2500.0);
        System.out.println("[AccountServer] Comptes initialisés : A001=1000, A002=500, A003=2500");
    }

    @Override
    public synchronized void deposit(String accountId, double amount)
            throws RemoteException, AccountNotFoundException, InvalidAmountException {

        if (amount <= 0)
            throw new InvalidAmountException(amount);

        if (!accounts.containsKey(accountId))
            throw new AccountNotFoundException(accountId);

        accounts.merge(accountId, amount, Double::sum);
        System.out.printf("[AccountServer] DEPOT   | Compte: %s | Montant: %.2f | Nouveau solde: %.2f%n",
                accountId, amount, accounts.get(accountId));
    }

    @Override
    public synchronized void withdraw(String accountId, double amount)
            throws RemoteException, AccountNotFoundException,
                   InsufficientFundsException, InvalidAmountException {

        if (amount <= 0)
            throw new InvalidAmountException(amount);

        if (!accounts.containsKey(accountId))
            throw new AccountNotFoundException(accountId);

        double balance = accounts.get(accountId);


        if (balance < amount)
            throw new InsufficientFundsException(accountId, balance, amount);

        accounts.put(accountId, balance - amount);
        System.out.printf("[AccountServer] RETRAIT | Compte: %s | Montant: %.2f | Nouveau solde: %.2f%n",
                accountId, amount, accounts.get(accountId));
    }

    @Override
    public synchronized double getBalance(String accountId)
            throws RemoteException, AccountNotFoundException {

        if (!accounts.containsKey(accountId))
            throw new AccountNotFoundException(accountId);

        double balance = accounts.get(accountId);
        System.out.printf("[AccountServer] SOLDE   | Compte: %s | Solde: %.2f%n", accountId, balance);
        return balance;
    }
}
