package Presentador;

import BD.ServidorBD;
import Modelo.Producto.*;
import java.awt.Color;
import java.awt.event.ItemEvent;
import static java.awt.event.ItemEvent.SELECTED;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorProductos{

    Producto producto = new Producto();
    Color verdeClaro = new Color(31, 192, 132);
    Color verdeOscuro = new Color(12, 72, 56);
    ServidorBD bd = new ServidorBD();
    int suma = 0;

    public PresentadorProductos() {

    }

    public void eliminarProducto(int codProducto) {
        bd.eliminarProducto(codProducto);
        listarTabla();
    }

    public String[][] listarTabla() {

        ArrayList<Producto> lista = bd.listarProductos();

        String productos[][] = new String[lista.size()][7];

        for (int i = 0; i < lista.size(); i++){
                productos[i][0]= "" + lista.get(i).getCodigo();
                productos[i][1]= lista.get(i).getDescripcion();
                productos[i][2]= lista.get(i).getMarca().getDescripcionM();
                productos[i][3]= lista.get(i).getRubro().getDescripcionR();
                productos[i][4]= "" + lista.get(i).getCosto();
                productos[i][5]= "" + lista.get(i).getPorcIVA();
                productos[i][6]= "" + lista.get(i).getMargenGanancia();
        }
        
        for(int i=0;i<productos.length;i++){
            System.out.println(Arrays.toString(productos[i]));
        }
        
        return productos;
    }
}
