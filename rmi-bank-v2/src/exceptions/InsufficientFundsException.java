package exceptions;

import java.rmi.RemoteException;

public class InsufficientFundsException extends RemoteException {
    public InsufficientFundsException(String accountId, double balance, double amount) {
        super(String.format("Solde insuffisant sur le compte %s : solde=%.2f, demandé=%.2f",
                accountId, balance, amount));
    }
}
