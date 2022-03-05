package Presentador;


import BD.ServidorBD;
import Modelo.Venta.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorVentas{

    ServidorBD bd = new ServidorBD();
    

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");

    public PresentadorVentas() {

    }

    public String[][] listarVentas(DefaultTableModel datos, long cuit, int codigoTipo) {        
        
        ArrayList<Venta> ve = bd.busquedaVenta(cuit, "b.numFactura", codigoTipo);
        String[][] ventas = new String[ve.size()][4];        
        
        for (int i = 0; i < ventas.length; i++){
                ventas[i][0]= ""+ve.get(i).getNroComprobante();
                ventas[i][1]= ""+ve.get(i).getFecha();
                ventas[i][2]= ""+ve.get(i).getTotal();
                ventas[i][3]= ""+ve.get(i).getCliente().getCuit();
        }
        return ventas;
    }

    public void cargarVentas(ArrayList<Venta> ve, DefaultTableModel datos) {
        datos.setNumRows(0);
        
        for(int i = 0;i<ve.size();i++){
            Object[] fila = {ve.get(i).getNroComprobante(),
                             ve.get(i).getFecha(),
                             ve.get(i).getTotal(),
                             ve.get(i).getCliente().getCuit()};            
            datos.addRow(fila);
        }
    }
}
