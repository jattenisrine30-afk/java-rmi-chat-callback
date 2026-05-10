package exceptions;

import java.rmi.RemoteException;

public class InvalidAmountException extends RemoteException {
    public InvalidAmountException(double amount) {
        super(String.format("Montant invalide : %.2f (doit être supérieur à 0)", amount));
    }
}
