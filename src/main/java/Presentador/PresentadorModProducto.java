/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentador;

import BD.ServidorBD;
import Modelo.Producto.Marca;
import Modelo.Producto.Producto;
import Modelo.Producto.Rubro;
import Modelo.Producto.TipoDeTalle;
import java.util.ArrayList;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorModProducto{

    ServidorBD bd = new ServidorBD();

    public PresentadorModProducto() {
        
    }

    public void modificarProducto(String id, String descripcion, String iva, String costo, String margen, String marca, String rubro) {
        Marca m = new Marca(bd.buscarCodMarca(marca),marca);
        Rubro r = new Rubro(bd.buscarCodRubro(rubro),rubro);
            
        Producto productoEnvio = new Producto(Integer.parseInt(id),descripcion,Double.parseDouble(iva),Double.parseDouble(costo)
                                            ,Double.parseDouble(margen),m,r,1);
        bd.modificarProducto(productoEnvio,m,r);     
    }
    
    public ArrayList<ArrayList<String>> cargarCombosM() {
        ArrayList<ArrayList<String>> combos = new ArrayList<ArrayList<String>>();
        ArrayList<String> marcaV = new ArrayList<>();
        ArrayList<String> rubroV = new ArrayList<>();
        ArrayList<String> tipoV = new ArrayList<>();
        ArrayList<String> colorV = new ArrayList<>();
        
        ArrayList<Marca> marca = bd.listarMarcas();
        ArrayList<TipoDeTalle> tipo = bd.listarTipoTalle();
        ArrayList<Modelo.Producto.ColorP> color = bd.listarColor();
        ArrayList<Rubro> rubro = bd.listarRubros();
        
        for(int i=0; i<marca.size();i++){
            marcaV.add(marca.get(i).getDescripcionM());
        }
        combos.add(marcaV);
        for(int i=0; i<rubro.size();i++){
            rubroV.add(rubro.get(i).getDescripcionR());
        }
        combos.add(rubroV);
        for(int i=0; i<tipo.size();i++){
            tipoV.add(tipo.get(i).getDescripcion());
        }
        combos.add(tipoV);
        for(int i=0; i<color.size();i++){
            colorV.add(color.get(i).getDescripcion());
        }
        combos.add(colorV);

        return combos;
    }    
    

    public ArrayList<String> rellenarCampos(String codigo) {
        ArrayList<String> producto = new ArrayList<String>();
        Producto p = bd.listarProducto(Integer.parseInt(codigo));
        
        producto.add(""+p.getCodigo());
        producto.add(p.getDescripcion());
        producto.add(""+p.getMarca().getDescripcionM());
        producto.add(""+p.getCosto());        
        producto.add(""+p.getPorcIVA());
        producto.add(""+p.getMargenGanancia());
        producto.add(""+p.getRubro());
        producto.add(""+p.getStock());                
        
        return producto;
    }

    
    
    
    
}
