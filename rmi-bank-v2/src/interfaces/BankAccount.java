package interfaces;

import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BankAccount extends Remote {

    void deposit(String accountId, double amount)
            throws RemoteException, AccountNotFoundException, InvalidAmountException;

    void withdraw(String accountId, double amount)
            throws RemoteException, AccountNotFoundException, InsufficientFundsException, InvalidAmountException;

    double getBalance(String accountId)
            throws RemoteException, AccountNotFoundException;
}
