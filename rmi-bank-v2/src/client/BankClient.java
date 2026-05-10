package client;


import interfaces.BankAccount;
import interfaces.TransferService;

import java.rmi.ServerException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class BankClient {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            BankAccount bankAccount         = (BankAccount)     registry.lookup("BankAccount");
            TransferService transferService = (TransferService) registry.lookup("TransferService");

            System.out.println("===========================================");
            System.out.println("   Client Bancaire RMI                    ");
            System.out.println("===========================================\n");

            //Soldes initiaux ──────────────────────────────────────
            System.out.println(">>> Soldes initiaux :");
            afficherSolde(bankAccount, "A001");
            afficherSolde(bankAccount, "A002");
            afficherSolde(bankAccount, "A003");

            //  Transfert normal
            System.out.println("\n>>> Transfert normal : 200.00 de A001 vers A002...");
            try {
                transferService.transfer("A001", "A002", 200.0);
                System.out.println("    Transfert réussi !");
            } catch (ServerException e) {
                afficherCause(e);
            }

            System.out.println("\n>>> Soldes après transfert :");
            afficherSolde(bankAccount, "A001");
            afficherSolde(bankAccount, "A002");

            //  Exception : compte inexistant
            System.out.println("\n>>> Test AccountNotFoundException : compte 'X999'...");
            try {
                bankAccount.getBalance("X999");
            } catch (ServerException e) {
                afficherCause(e);
            }

            // Exception : montant invalide
            System.out.println("\n>>> Test InvalidAmountException : depot de -50...");
            try {
                bankAccount.deposit("A001", -50.0);
            } catch (ServerException e) {
                afficherCause(e);
            }

            //  Exception : solde insuffisant
            System.out.println("\n>>> Test InsufficientFundsException : retrait 9999 sur A002...");
            try {
                bankAccount.withdraw("A002", 9999.0);
            } catch (ServerException e) {
                afficherCause(e);
            }

            //Exception : meme compte source et destination
            System.out.println("\n>>> Test TransferException : transfert A001 vers A001...");
            try {
                transferService.transfer("A001", "A001", 100.0);
            } catch (ServerException e) {
                afficherCause(e);
            }

            // Test concurrence : 2 personnes retirent en meme temps
            System.out.println("\n>>> Test CONCURRENCE : 2 threads retirent 800 sur A001 en meme temps...");
            System.out.println("    (Solde A001 = 800 -> un seul doit reussir)\n");

            Thread client1 = new Thread(() -> {
                try {
                    System.out.println("    [Thread-1] Tentative retrait 800 sur A001...");
                    bankAccount.withdraw("A001", 800.0);
                    System.out.println("    [Thread-1] Retrait reussi !");
                } catch (ServerException e) {
                    System.out.println("    [Thread-1] [" + e.getCause().getClass().getSimpleName() + "] " + e.getCause().getMessage());
                } catch (Exception e) {
                    System.out.println("    [Thread-1] [ERREUR] " + e.getMessage());
                }
            });

            Thread client2 = new Thread(() -> {
                try {
                    System.out.println("    [Thread-2] Tentative retrait 800 sur A001...");
                    bankAccount.withdraw("A001", 800.0);
                    System.out.println("    [Thread-2] Retrait reussi !");
                } catch (ServerException e) {
                    System.out.println("    [Thread-2] [" + e.getCause().getClass().getSimpleName() + "] " + e.getCause().getMessage());
                } catch (Exception e) {
                    System.out.println("    [Thread-2] [ERREUR] " + e.getMessage());
                }
            });

            client1.start();
            client2.start();

            try {
                client1.join();
                client2.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("\n>>> Solde final A001 (doit etre 0 ou 800, jamais -800) :");
            afficherSolde(bankAccount, "A001");

            System.out.println("\n===========================================");
            System.out.println("   Fin des tests                          ");
            System.out.println("===========================================");

        } catch (Exception e) {
            System.err.println("[ERREUR FATALE] " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void afficherSolde(BankAccount bank, String accountId) {
        try {
            double solde = bank.getBalance(accountId);
            System.out.printf("   Compte %-6s : %.2f MAD%n", accountId, solde);
        } catch (ServerException e) {
            System.out.println("   [" + e.getCause().getClass().getSimpleName() + "] " + e.getCause().getMessage());
        } catch (Exception e) {
            System.out.println("   [ERREUR] " + e.getMessage());
        }
    }


    private static void afficherCause(ServerException e) {
        if (e.getCause() != null) {
            System.out.println("    [" + e.getCause().getClass().getSimpleName() + "] " + e.getCause().getMessage());
        } else {
            System.out.println("    [ServerException] " + e.getMessage());
        }
    }
}