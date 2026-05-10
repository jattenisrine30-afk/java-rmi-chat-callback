package transfer;

import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import exceptions.TransferException;
import interfaces.BankAccount;
import interfaces.TransferService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class TransferServiceImpl extends UnicastRemoteObject implements TransferService {

    private final BankAccount bankAccount;

    public TransferServiceImpl() throws RemoteException {
        super();
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            bankAccount = (BankAccount) registry.lookup("BankAccount");
            System.out.println("[TransferService] Connecté au Account Server avec succès.");
        } catch (Exception e) {
            throw new RemoteException("Impossible de se connecter au Account Server.", e);
        }
    }

    @Override
    public void transfer(String fromAccountId, String toAccountId, double amount)
            throws RemoteException, AccountNotFoundException,
                   InsufficientFundsException, InvalidAmountException, TransferException {

        // Vérification montant invalide
        if (amount <= 0)
            throw new InvalidAmountException(amount);

        // Vérification : même compte source et destination
        if (fromAccountId.equals(toAccountId))
            throw new TransferException(fromAccountId, toAccountId, amount,
                    "Les comptes source et destination sont identiques");

        System.out.printf("[TransferService] TRANSFERT | De: %s → Vers: %s | Montant: %.2f%n",
                fromAccountId, toAccountId, amount);

        // Appel RMI au AccountServer — synchronized côté serveur gère la concurrence
        bankAccount.withdraw(fromAccountId, amount);
        bankAccount.deposit(toAccountId, amount);

        System.out.printf("[TransferService] TRANSFERT réussi | %.2f transféré de %s vers %s%n",
                amount, fromAccountId, toAccountId);
    }
}
