/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentador;

import BD.ServidorBD;
import Modelo.Producto.Auxiliar;
import Modelo.Producto.ColorP;
import Modelo.Producto.Marca;
import Modelo.Producto.Producto;
import Modelo.Producto.Talle;
import Modelo.Producto.TipoDeTalle;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorGestionarProd{

    ServidorBD bd = new ServidorBD();

    public PresentadorGestionarProd() {
        
    }

    public void agregarMarca(String Marca) {
        Marca marca = new Marca(bd.ultimoCodMarca(), Marca);
        bd.agregarMarca(marca);
//        listarTabla();
    }

    public void agregarColor(String color) {
        ColorP co = new ColorP(bd.ultimoCodColor(), color);
        bd.agregarColor(co);
//        listarTabla();
    }

    public boolean existe(String codigo) {
        if (bd.existe(codigo)) {
            return true;
        }
        return false;
    }

    public ArrayList<ArrayList<String>> cargarCombos() {            
        ArrayList<ArrayList<String>> combos = new ArrayList<ArrayList<String>>();
        
        ArrayList<String> tipoV = new ArrayList<>();
        ArrayList<String> colorV = new ArrayList<>();
        
        ArrayList<TipoDeTalle> tipo = bd.listarTipoTalle();
        ArrayList<Modelo.Producto.ColorP> color = bd.listarColor();
        
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

    public String[][] listarTablaColor() {
        
        ArrayList<ColorP> listaColor = bd.listarColor();
        String[][] colores = new String[listaColor.size()][1];
        
        for(int i=0;i<listaColor.size();i++){
            colores[i][0] = listaColor.get(i).getDescripcion();
        }
        
        return colores;           
    }
    
    public  String[][] listarTablaMarca() {
        
        ArrayList<Marca> listaMarcas = bd.listarMarcas();
        String[][] marcas = new String[listaMarcas.size()][1];
        
        for(int i=0;i<listaMarcas.size();i++){
            marcas[i][0] = listaMarcas.get(i).getDescripcionM();
        }
        
        return marcas; 
    }

    
    public ArrayList<String> agregarCaracteristicas(String codigo) {
        ArrayList<String> producto = new ArrayList<String>();

        Producto p = bd.buscarProducto(codigo);
        
        producto.add(""+p.getCodigo());
        producto.add(p.getDescripcion());
        producto.add(""+p.getMarca().getDescripcionM());
        producto.add(""+p.getCosto());        
        producto.add(""+p.getPorcIVA());
        producto.add(""+p.getMargenGanancia());
        producto.add(""+p.getRubro());
        producto.add(""+p.getStock().getCantidad()); 
        
        return producto;
    }

    
    public void actualizarStock(String codigo, String cant, String talle, String color) {
        int scant = 0;
        Producto p = bd.buscarProducto(codigo);
        Talle t = new Talle(bd.buscarCodTalle(talle), talle);
        ColorP co = new ColorP(bd.buscarCodColor(color), color);

        bd.actualizarStockG(p.getCodigo(), Integer.parseInt(cant), t, co);
        JOptionPane.showMessageDialog(null, "Stock actualizado");
    }

    public ArrayList<String> llenarCombos(String tipo) {
        ArrayList<String> talles = new ArrayList<String>();

        ServidorBD bd = new ServidorBD();
        int cod = bd.buscarCodTipo(tipo);
        ArrayList<Talle> talle = bd.buscarTalle(cod);
        
        for (int i = 0; i < talle.size(); i++) {
            talles.add(talle.get(i).getDescripcion());
        }
        return talles;
    }

    public String[][] listarCaracteristicas(String id) {
        ArrayList<Auxiliar> listaCarac = bd.listarCaracteristicas(id);
        String[][] lista = new String[listaCarac.size()][3];
        
        for(int i = 0;i<listaCarac.size();i++){
            System.out.println(listaCarac.get(i));
        }
        for(int i = 0;i<listaCarac.size();i++){
            lista[i][0] = listaCarac.get(i).getTalle();
            lista[i][1] = listaCarac.get(i).getColor();
            lista[i][2] = listaCarac.get(i).getCantidad();
        }
        
        return lista;
    }
}
