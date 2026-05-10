package interfaces;

import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import exceptions.TransferException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TransferService extends Remote {

    void transfer(String fromAccountId, String toAccountId, double amount)
            throws RemoteException, AccountNotFoundException,
                   InsufficientFundsException, InvalidAmountException, TransferException;
}
