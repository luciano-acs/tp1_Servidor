package Presentador;

import BD.ServidorBD;
import Modelo.ClienteT.Cliente;
import java.util.ArrayList;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorClientes{
    
    ServidorBD bd = new ServidorBD();
    

    public PresentadorClientes() {
        
    }

    public String[][] cargarTabla() {       
                
        ArrayList<Cliente> lista = bd.listarClientes();
        
        String[][] clientes = new String[lista.size()][5];
        
        for(int i = 0;i<lista.size();i++){
            clientes[i][0] = lista.get(i).getCuit();
            clientes[i][1] = lista.get(i).getRazonSocial();
            clientes[i][2] = lista.get(i).getDomicilio();
            clientes[i][3] = lista.get(i).getEmail();
            clientes[i][4] = lista.get(i).getCondicion();
        }
        return clientes;
    }
}
