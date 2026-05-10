package transfer;

import interfaces.TransferService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class TransferServer {

    public static void main(String[] args) {
        try {
            TransferService transferService = new TransferServiceImpl();
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            registry.rebind("TransferService", transferService);

            System.out.println("===========================================");
            System.out.println("  Transfer Server démarré                 ");
            System.out.println("  Service enregistré : 'TransferService'  ");
            System.out.println("===========================================");
            System.out.println("En attente de connexions...");

        } catch (Exception e) {
            System.err.println("[ERREUR] TransferServer : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
