package server;

import interfaces.BankAccount;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class AccountServer {

    public static void main(String[] args) {
        try {
            BankAccount bankAccount = new BankAccountImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("BankAccount", bankAccount);

            System.out.println("===========================================");
            System.out.println("  Account Server démarré sur le port 1099 ");
            System.out.println("  Service enregistré sous : 'BankAccount'  ");
            System.out.println("===========================================");
            System.out.println("En attente de connexions...");

        } catch (Exception e) {
            System.err.println("[ERREUR] AccountServer : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
