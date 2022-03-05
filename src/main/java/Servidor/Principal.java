package Servidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Luciano Acosta
 */
public class Principal {

    public static void main(String[] args) {
        try {
            Registry miRegistry = LocateRegistry.createRegistry(6000);
            miRegistry.rebind("RemoteInterface", new ServerImplements());
            System.out.println("Servidor Conectado");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
