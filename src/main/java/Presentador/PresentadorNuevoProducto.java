/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentador;

import BD.ServidorBD;
import Modelo.Producto.Marca;
import Modelo.Producto.Producto;
import Modelo.Producto.Rubro;
import Modelo.Producto.Stock;
import java.util.ArrayList;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorNuevoProducto{

    ServidorBD bd = new ServidorBD();

    public PresentadorNuevoProducto() {
        
    }

    public void cargarProducto(String id, String descripcion, String iva, String costo, String margen, String marca, String rubro, String cantidad) {
        Marca m = new Marca(bd.buscarCodMarca(marca),marca);
        Rubro r = new Rubro(bd.buscarCodRubro(rubro),rubro);
        
        Stock s = new Stock(Integer.parseInt(id),Integer.parseInt(cantidad));
        Producto productoEnvio = new Producto(Integer.parseInt(id),descripcion,Double.parseDouble(iva),Double.parseDouble(costo)
                                            ,Double.parseDouble(margen),m,r,s,1);
        bd.agregarProducto(productoEnvio,s,m,r);   
        
        PresentadorProductos pp = new PresentadorProductos();
        pp.listarTabla();
    }
    
    public ArrayList<ArrayList<String>> cargarCombos(){
        ArrayList<ArrayList<String>> combos = new ArrayList<ArrayList<String>>();
        ArrayList<String> marcaV = new ArrayList<>();
        ArrayList<String> rubroV = new ArrayList<>();
        
        ArrayList<Marca> marca = bd.listarMarcas();
        ArrayList<Rubro> rubro = bd.listarRubros();
        
        for(int i=0; i<marca.size();i++){
            marcaV.add(marca.get(i).getDescripcionM());
        }
        combos.add(marcaV);
        for(int i=0; i<rubro.size();i++){
            rubroV.add(rubro.get(i).getDescripcionR());
        }
        combos.add(rubroV);
        
        return combos;
    }
}
