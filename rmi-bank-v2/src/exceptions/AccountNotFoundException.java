package exceptions;

import java.rmi.RemoteException;

public class AccountNotFoundException extends RemoteException {
    public AccountNotFoundException(String accountId) {
        super("Compte introuvable : " + accountId);
    }
}
