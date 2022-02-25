package Presentador;

import Servidor.ServidorBD;
import Modelo.Producto.*;
import Vista.pListarProductos;
import Vista.pModProductos;
import Vista.pProductos;
import Vista.pgestiones;
import Vista.vistaMenu;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import static java.awt.event.ItemEvent.SELECTED;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.lang.System.out;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorProductos implements ActionListener, java.awt.event.ItemListener{

    pListarProductos listaProd = new pListarProductos();
    pProductos productos = new pProductos();
    Producto producto = new Producto();
    pModProductos modProductos = new pModProductos();
    pgestiones gestionar = new pgestiones();
    vistaMenu menu = new vistaMenu();
    Color verdeClaro = new Color(31,192,132);
    Color verdeOscuro = new Color(12,72,56);
    ServidorBD bd = new ServidorBD();
    int suma = 0;
            
    public PresentadorProductos(pListarProductos lista) {
        this.listaProd = lista;
        listaProd.btnBuscarP.addActionListener(this);
        listaProd.jtfBuscar.addActionListener(this);
        listaProd.btnEliminar.addActionListener(this);
        
        listaProd.jLabel3.setText("");
        listaProd.jtfBuscar.setText("");
        
    }    
    
    public PresentadorProductos(pProductos prod){
        this.productos = prod; 
        productos.btnAgregarProdSolo.addActionListener(this);
       
        productos.jtfCantidad.setEnabled(false);
        productos.jtfCantidad.setText(""+0);   
        productos.jtfCosto.setText("");
        productos.jtfDescripcion.setText("");
        productos.jtfIVA.setText("");
        productos.jtfMargen.setText("");
        productos.jtfNombre.setText("");
    }

    public PresentadorProductos(pModProductos mod, vistaMenu menu) {
        
        this.modProductos = mod;
        modProductos.cbTipo.addItemListener(this);
        modProductos.btnBuscarP.addActionListener(this);
        modProductos.btnModProd.addActionListener(this);
        
        modProductos.cbColor.removeAllItems();
        modProductos.cbMarca.removeAllItems();
        modProductos.cbRubro.removeAllItems();
        modProductos.cbTalle.removeAllItems();
        modProductos.cbTipo.removeAllItems();
        modProductos.jtfCantidad.setText("");
        modProductos.jtfCosto.setText("");
        modProductos.jtfDescripcion.setText("");
        modProductos.jtfIVA.setText("");
        modProductos.jtfMargen.setText("");
        modProductos.jtfNombre.setText("");
    }
    
    public PresentadorProductos(pgestiones gestion){
        this.gestionar = gestion;
        gestionar.cbTipo.addItemListener(this);
        gestionar.cbTarea.addItemListener(this);
        gestionar.btnBuscarP.addActionListener(this);
        gestionar.btnAgregarColor.addActionListener(this);
        gestionar.btnAgregarMarca.addActionListener(this);
        gestionar.btnAgregarCantidad.addActionListener(this);
        
        gestionar.jtfCantidad.setText("");
        gestionar.jtfDescripcion.setText("");
        gestionar.jtfMarca.setText("");
        gestionar.jtfNombre.setText("");
        gestionar.jtfNuvoColor.setText("");
        gestionar.jtfNvaMarca.setText("");
        gestionar.jtfDescripcion.setEnabled(false);
        gestionar.jtfMarca.setEnabled(false);
        gestionar.jlLimite.setText(""+0);
        DefaultTableModel datos = (DefaultTableModel) gestionar.jtCaracteristicas.getModel();
        for(int i = 0;i<gestionar.jtCaracteristicas.getRowCount();i++){
            datos.removeRow(i);
            i-=1;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource().equals(listaProd.btnBuscarP)){
            buscarProductoTabla();
        }
        if(e.getSource().equals(listaProd.btnEliminar)){
            eliminarProducto();
            listarTabla();
        }
        if(e.getSource().equals(productos.btnAgregarProdSolo)){
            agregarProductoSolo();
        }
        if(e.getSource().equals(modProductos.btnBuscarP)){
            if(modProductos.jtfNombre.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Indique el producto a modificar");
            }else{
                rellenarCampos();
            }
        }
        if(e.getSource().equals(modProductos.btnModProd)){            
            System.out.println("entro a modificar producto");
            modProducto(); 
        }
        if(e.getSource().equals(gestionar.btnAgregarColor)){
            if(gestionar.jtfNuvoColor.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Indique el color a incluir");
            }else{
                ColorP color = new ColorP(bd.ultimoCodColor(),gestionar.jtfNuvoColor.getText());
                bd.agregarColor(color);
                listarTablaGestion();
                gestionar.jtfNuvoColor.setText("");
            }            
            
        }
        if(e.getSource().equals(gestionar.btnAgregarMarca)){
            if(gestionar.jtfNvaMarca.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Indique la marca a incluir");
            }else{
                Marca marca = new Marca(bd.ultimoCodMarca(),gestionar.jtfNvaMarca.getText());
                bd.agregarMarca(marca);
                listarTablaGestion();
                gestionar.jtfNvaMarca.setText("");
            }
        }
        if(e.getSource().equals(gestionar.btnBuscarP)){
            agregarCaracteristica();
        }
        if(e.getSource().equals(gestionar.btnAgregarCantidad)){
            if(gestionar.jtfCantidad.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Indique la cantidad a incluir");
            }else{
                actualizarStock();
                agregarCaracteristica();
            }
        }
        e.setSource("");
    }

    private void agregarProductoSolo() {
        if(productos.jtfNombre.getText().isEmpty()||productos.jtfDescripcion.getText().isEmpty()||
                productos.jtfIVA.getText().isEmpty()||productos.jtfCosto.getText().isEmpty()||
                productos.jtfMargen.getText().isEmpty()||productos.cbMarca.getSelectedItem().toString().isEmpty()||
                productos.cbRubro.getSelectedItem().toString().isEmpty()||productos.jtfCantidad.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Campos vacios");
        }else{
            int codigo = parseInt(productos.jtfNombre.getText());
            String descripcion = productos.jtfDescripcion.getText();
            double iva = parseDouble(productos.jtfIVA.getText());
            double costo = parseDouble(productos.jtfCosto.getText());
            double margen = parseDouble(productos.jtfMargen.getText());
        
            String marca = (String) productos.cbMarca.getSelectedItem();       
            String rubro = (String) productos.cbRubro.getSelectedItem();
            int stock = parseInt(productos.jtfCantidad.getText());
        
            Marca m = new Marca(bd.buscarCodMarca(marca),marca);
            Rubro r = new Rubro(bd.buscarCodRubro(rubro),rubro);
        
            Stock s = new Stock(codigo,stock);
            Producto productoEnvio = new Producto(codigo,descripcion,iva,costo,margen,m,r,s,1);
            bd.agregarProducto(productoEnvio,s,m,r);            
            listarTabla();       
            showMessageDialog(null, "Producto agregado con exito");
        }
        
    }
    
    public void listarTabla() {
        DefaultTableModel datos = (DefaultTableModel) listaProd.jtProductos.getModel();
        datos.setNumRows(0);
        
        ArrayList<Producto> lista = bd.listarProductos();
        
        for(int i = 0;i<lista.size();i++){
            Object[] fila = {lista.get(i).getCodigo(),
                             lista.get(i).getDescripcion(),
                             lista.get(i).getMarca().getDescripcionM(),
                             lista.get(i).getRubro().getDescripcionR(),
                             lista.get(i).getCosto(),
                             lista.get(i).getPorcIVA(),
                             lista.get(i).getMargenGanancia()
                             };
            
            datos.addRow(fila);
        }
    }
    
    private void buscarProductoTabla() {
        
        String codigo = listaProd.jtfBuscar.getText();
        out.println(codigo);
        for(int i = 0;i<listaProd.jtProductos.getRowCount();i++){
            if(listaProd.jtProductos.getValueAt(i, 0).toString().equals(codigo)){
                listaProd.jLabel3.setText("");
                listaProd.jtProductos.changeSelection(i, 0, false, false);
                break;
            }
            else{
                listaProd.jLabel3.setText("El producto no se encuentra registrado");
            }
        }
    }
    
    public void cargarCombos(){
        productos.cbMarca.removeAllItems();
//        productos.cbTalle.removeAllItems();
//        productos.cbColor.removeAllItems();
//        productos.cbTipo.removeAllItems();
        productos.cbRubro.removeAllItems();
        
        ArrayList<Marca> marca = bd.listarMarcas();
        ArrayList<TipoDeTalle> tipo = bd.listarTipoTalle();
        ArrayList<Modelo.Producto.ColorP> color = bd.listarColor();
        ArrayList<Rubro> rubro = bd.listarRubros();
        
        for(int i =0;i<marca.size();i++){
            productos.cbMarca.addItem(marca.get(i).getDescripcionM());
        }
        for(int i =0; i<rubro.size();i++){
            productos.cbRubro.addItem(rubro.get(i).getDescripcionR());
        }

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange()==SELECTED){
            gestionar.cbTalle.removeAllItems();
            if(gestionar.cbTipo.getSelectedIndex()>-1){
                ServidorBD bd = new ServidorBD();
                int cod = bd.buscarCodTipo(gestionar.cbTipo.getSelectedItem().toString());        
                ArrayList<Talle> talle =  bd.buscarTalle(cod);
                for(int i =0;i<talle.size();i++){
                    gestionar.cbTalle.addItem(talle.get(i).getDescripcion());            
                }
            }
        }
        if(e.getStateChange()==SELECTED){
            modProductos.cbTalle.removeAllItems();
            if(modProductos.cbTipo.getSelectedIndex()>-1){
                ServidorBD bd = new ServidorBD();
                int cod = bd.buscarCodTipo(modProductos.cbTipo.getSelectedItem().toString());        
                ArrayList<Talle> talle =  bd.buscarTalle(cod);
                for(int i =0;i<talle.size();i++){
                    modProductos.cbTalle.addItem(talle.get(i).getDescripcion());            
                }
            }
        }
        if(e.getStateChange()==SELECTED){
            if(gestionar.cbTarea.getSelectedIndex()>-1){
                if(gestionar.cbTarea.getSelectedItem().equals("Marca")){
                    gestionar.jlMarca.setForeground(verdeClaro);
                    gestionar.jlColor.setForeground(verdeOscuro);
                    gestionar.jlStock.setForeground(verdeOscuro);
                    
                    gestionar.jtfNvaMarca.setEnabled(true);
                    gestionar.btnAgregarMarca.setEnabled(true);
                    gestionar.jtMarca.setEnabled(true);
                    
                    gestionar.jtfNuvoColor.setEnabled(false);
                    gestionar.btnAgregarColor.setEnabled(false);
                    gestionar.jtColor.setEnabled(false);
                    
                    gestionar.jtfCantidad.setEnabled(false);
                    gestionar.jtfDescripcion.setEnabled(false);
                    gestionar.jtfMarca.setEnabled(false);
                    gestionar.jtfNombre.setEnabled(false);
                    gestionar.btnAgregarCantidad.setEnabled(false);
                    gestionar.btnBuscarP.setEnabled(false);
                    gestionar.cbColor.setEnabled(false);
                    gestionar.cbTalle.setEnabled(false);
                    gestionar.cbTipo.setEnabled(false);
                    gestionar.jtCaracteristicas.setEnabled(false);
                }
                if(gestionar.cbTarea.getSelectedItem().equals("Color")){
                    gestionar.jlMarca.setForeground(verdeOscuro);
                    gestionar.jlColor.setForeground(verdeClaro);
                    gestionar.jlStock.setForeground(verdeOscuro);
                    
                    gestionar.jtfNuvoColor.setEnabled(true);
                    gestionar.btnAgregarColor.setEnabled(true);
                    gestionar.jtColor.setEnabled(true);
                    
                    gestionar.jtfNvaMarca.setEnabled(false);
                    gestionar.btnAgregarMarca.setEnabled(false);
                    gestionar.jtMarca.setEnabled(false);
                    
                    gestionar.jtfCantidad.setEnabled(false);
                    gestionar.jtfDescripcion.setEnabled(false);
                    gestionar.jtfMarca.setEnabled(false);
                    gestionar.jtfNombre.setEnabled(false);
                    gestionar.btnAgregarCantidad.setEnabled(false);
                    gestionar.btnBuscarP.setEnabled(false);
                    gestionar.cbColor.setEnabled(false);
                    gestionar.cbTalle.setEnabled(false);
                    gestionar.cbTipo.setEnabled(false);
                    gestionar.jtCaracteristicas.setEnabled(false);
                }
                if(gestionar.cbTarea.getSelectedItem().equals("Stock")){
                    gestionar.jlMarca.setForeground(verdeOscuro);
                    gestionar.jlColor.setForeground(verdeOscuro);
                    gestionar.jlStock.setForeground(verdeClaro);
                    
                    gestionar.jtfCantidad.setEnabled(true);
                    gestionar.jtfDescripcion.setEnabled(true);
                    gestionar.jtfMarca.setEnabled(true);
                    gestionar.jtfNombre.setEnabled(true);
                    gestionar.btnAgregarCantidad.setEnabled(true);
                    gestionar.btnBuscarP.setEnabled(true);
                    gestionar.cbColor.setEnabled(true);
                    gestionar.cbTalle.setEnabled(true);
                    gestionar.cbTipo.setEnabled(true);
                    gestionar.jtCaracteristicas.setEnabled(true);
                    
                    gestionar.jtfNuvoColor.setEnabled(false);
                    gestionar.btnAgregarColor.setEnabled(false);
                    gestionar.jtColor.setEnabled(false);
                    
                    gestionar.jtfNvaMarca.setEnabled(false);
                    gestionar.btnAgregarMarca.setEnabled(false);
                    gestionar.jtMarca.setEnabled(false);
                }
                
            }
        }
    }

    void cargarCombosM() {
        modProductos.cbMarca.removeAllItems();
        modProductos.cbTalle.removeAllItems();
        modProductos.cbColor.removeAllItems();
        modProductos.cbTipo.removeAllItems();
        modProductos.cbRubro.removeAllItems();
        modProductos.jtfCantidad.setEnabled(false);
        modProductos.cbTipo.setEnabled(false);
        modProductos.cbTalle.setEnabled(false);
        modProductos.cbColor.setEnabled(false);
        
        ArrayList<Marca> marca = bd.listarMarcas();
        ArrayList<TipoDeTalle> tipo = bd.listarTipoTalle();
        ArrayList<Modelo.Producto.ColorP> color = bd.listarColor();
        ArrayList<Rubro> rubro = bd.listarRubros();
        
        for(int i =0;i<marca.size();i++){
            modProductos.cbMarca.addItem(marca.get(i).getDescripcionM());
        }
        for(int i =0;i<tipo.size();i++){
            modProductos.cbTipo.addItem(tipo.get(i).getDescripcion());
        }
        for(int i =0;i<color.size();i++){
            modProductos.cbColor.addItem(color.get(i).getDescripcion());
        }
        for(int i =0; i<rubro.size();i++){
            modProductos.cbRubro.addItem(rubro.get(i).getDescripcionR());
        }
    }

    private void rellenarCampos() {
        int cod = parseInt(modProductos.jtfNombre.getText());
        
        Producto p = bd.listarProducto(cod);
        
        modProductos.jtfDescripcion.setText(p.getDescripcion());
        modProductos.jtfCosto.setText(valueOf(p.getCosto()));
        modProductos.jtfIVA.setText(valueOf(p.getPorcIVA()));
        modProductos.jtfMargen.setText(valueOf(p.getMargenGanancia()));
        modProductos.jtfNombre.setEnabled(false);
    }

    private void modProducto() {
        if(!modProductos.jtfNombre.getText().isEmpty()&&!modProductos.jtfDescripcion.getText().isEmpty()&&
                !modProductos.jtfIVA.getText().isEmpty()&& !modProductos.jtfCosto.getText().isEmpty()&&
                !modProductos.jtfMargen.getText().isEmpty()&&!modProductos.cbMarca.getSelectedItem().toString().isEmpty()&&
                !modProductos.cbRubro.getSelectedItem().toString().isEmpty()){
            int codigo = parseInt(modProductos.jtfNombre.getText());
            String descripcion = modProductos.jtfDescripcion.getText();
            double iva = parseDouble(modProductos.jtfIVA.getText());
            double costo = parseDouble(modProductos.jtfCosto.getText());
            double margen = parseDouble(modProductos.jtfMargen.getText());
            
            String marca = (String) modProductos.cbMarca.getSelectedItem();       
            String rubro = (String) modProductos.cbRubro.getSelectedItem();
        
            Marca m = new Marca(bd.buscarCodMarca(marca),marca);
            Rubro r = new Rubro(bd.buscarCodRubro(rubro),rubro);
            
            Producto productoEnvio = new Producto(codigo,descripcion,iva,costo,margen,m,r,1);
            bd.modificarProducto(productoEnvio,m,r);
            listarTabla();
            modProductos.jtfNombre.setEnabled(true);        
            showMessageDialog(null, "Producto modificado con exito");
        }else{
            JOptionPane.showMessageDialog(null,"Campos vacios en la modificacion");
        }      
    }

    private void eliminarProducto() {
        DefaultTableModel datos = (DefaultTableModel) listaProd.jtProductos.getModel(); 
        int fila = listaProd.jtProductos.getSelectedRow();
        if(fila==-1){
            JOptionPane.showMessageDialog(null, "Ninguna fila seleccionada");
        }else{
            if(listaProd.jtProductos.getValueAt(fila, 0)!=null){
                int codProducto = (int) listaProd.jtProductos.getValueAt(fila, 0);
                bd.eliminarProducto(codProducto);
            } 
        }
    }

    public void cargarCombosGestiones() {
        gestionar.cbTalle.removeAllItems();
        gestionar.cbColor.removeAllItems();
        gestionar.cbTipo.removeAllItems();        
        
        ArrayList<TipoDeTalle> tipo = bd.listarTipoTalle();
        ArrayList<Modelo.Producto.ColorP> color = bd.listarColor();
        
        for(int i =0;i<tipo.size();i++){
            gestionar.cbTipo.addItem(tipo.get(i).getDescripcion());
        }
        for(int i =0;i<color.size();i++){
            gestionar.cbColor.addItem(color.get(i).getDescripcion());
        }
    }

    public void listarTablaGestion() {
        DefaultTableModel datosColor = (DefaultTableModel) gestionar.jtColor.getModel();
        datosColor.setNumRows(0);
        
        ArrayList<ColorP> listaColor = bd.listarColor();
        
        for(int i = 0;i<listaColor.size();i++){
            Object[] fila = {listaColor.get(i).getDescripcion()
                            };
            
            datosColor.addRow(fila);
        }
        
        DefaultTableModel datosMarca = (DefaultTableModel) gestionar.jtMarca.getModel();
        datosMarca.setNumRows(0);
        
        ArrayList<Marca> listaMarcas = bd.listarMarcas();
        
        for(int i = 0;i<listaMarcas.size();i++){
            Object[] fila = {listaMarcas.get(i).getDescripcionM()
                            };
            
            datosMarca.addRow(fila);
        }
    }

    private void agregarCaracteristica() {
        if(gestionar.jtfNombre.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"Indique el producto a gestionar");
        }else{
            if(bd.existe(gestionar.jtfNombre.getText())){
                DefaultTableModel datosProductos = (DefaultTableModel) gestionar.jtCaracteristicas.getModel();
                datosProductos.setNumRows(0);
        
                Producto p = bd.buscarProducto(gestionar.jtfNombre.getText());
                gestionar.jtfDescripcion.setText(p.getDescripcion());
                gestionar.jtfMarca.setText(p.getMarca().getDescripcionM());
                gestionar.jtfDescripcion.setEnabled(false);
                gestionar.jtfMarca.setEnabled(false);
                gestionar.jlLimite.setText(""+p.getStock().getCantidad());
                
                ArrayList<Auxiliar> listaCarac = bd.listarCaracteristicas(""+p.getCodigo());
        
                for(int i = 0;i<listaCarac.size();i++){
                    Object[] fila = {listaCarac.get(i).getTalle(),
                                    listaCarac.get(i).getColor(),
                                    listaCarac.get(i).getCantidad(),
                                    };
            
                datosProductos.addRow(fila);
                }
            }else{
                showMessageDialog(null, "El producto no exite \n Agregue producto en la seccion agregar producto");
                gestionar.jtfNombre.setText("");
            }
        }
    }

    private void actualizarStock() {
        String codigo = gestionar.jtfNombre.getText();
        int cant = Integer.parseInt(gestionar.jtfCantidad.getText());
        int scant = 0;
        Producto p = bd.buscarProducto(codigo);    
        Talle t = new Talle(bd.buscarCodTalle((String) gestionar.cbTalle.getSelectedItem()),(String) gestionar.cbTalle.getSelectedItem());
        ColorP co = new ColorP(bd.buscarCodColor((String) gestionar.cbColor.getSelectedItem()),(String) gestionar.cbColor.getSelectedItem());
        
        
        bd.actualizarStockG(p.getCodigo(),cant,t,co);
        JOptionPane.showMessageDialog(null,"Stock actualizado");
         
    }
    
}
