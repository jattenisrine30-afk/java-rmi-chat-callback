package exceptions;

import java.rmi.RemoteException;

public class TransferException extends RemoteException {
    public TransferException(String fromId, String toId, double amount, String reason) {
        super(String.format("Échec du transfert de %s vers %s (%.2f) : %s",
                fromId, toId, amount, reason));
    }
}
