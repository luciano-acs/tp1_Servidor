/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import BD.ServidorBD;
import Presentador.PresentadorClientes;
import Presentador.PresentadorFactura;
import Presentador.PresentadorGestionarProd;
import Presentador.PresentadorInicio;
import Presentador.PresentadorModProducto;
import Presentador.PresentadorNuevaVenta;
import Presentador.PresentadorNuevoCliente;
import Presentador.PresentadorNuevoProducto;
import Presentador.PresentadorProductos;
import Presentador.PresentadorVentas;
import RMIInterfaces.RemoteInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luciano Acosta
 */
public class ServerImplements extends UnicastRemoteObject implements RemoteInterface{

    ServidorBD bd = new ServidorBD();
    PresentadorInicio inicio = new PresentadorInicio();
    PresentadorNuevoCliente nuevoCliente = new PresentadorNuevoCliente();
    PresentadorClientes clientes = new PresentadorClientes();
    PresentadorFactura facturaP = new PresentadorFactura();
    PresentadorProductos productosP = new PresentadorProductos();
    PresentadorVentas ventasP = new PresentadorVentas();
    PresentadorModProducto modificarP = new PresentadorModProducto();
    PresentadorNuevoProducto nuevoProd = new PresentadorNuevoProducto();
    PresentadorGestionarProd gestionProd = new PresentadorGestionarProd();
    PresentadorNuevaVenta ventasNueva = new PresentadorNuevaVenta();
    
    public ServerImplements() throws RemoteException {
        super();
    }   

    @Override
    public ArrayList<String> ingresarSistema(String legajo, String contraseña) {
        ArrayList<String> ingresarSistema = inicio.ingresarSistema(legajo,contraseña);
        return ingresarSistema;
    }

    @Override
    public void nuevoCliente(String cuit, String condicion, String domicilio, String email, String razon) {
        nuevoCliente.nuevoCliente(cuit,condicion,domicilio,email,razon);
    }

    @Override
    public ArrayList<String> cargarCombos() {
        ArrayList<String> combos = nuevoCliente.cargarCombos();
        return combos;
    }

    @Override
    public String[][] cargarTabla() {
        String[][] tablaClientes = clientes.cargarTabla();
        return tablaClientes;
    }

    @Override
    public ArrayList<String> buscarFactura(String id, String cuit) {
        ArrayList<String> factura = facturaP.buscarFactura(id,cuit);
        return factura;
    }

    @Override
    public void eliminarProducto(int codProducto) {
        productosP.eliminarProducto(codProducto);
    }

    @Override
    public String[][] listarTablaProductos() {
        String[][] productos = productosP.listarTabla();
        return productos;
    }

    @Override
    public int cargarId() {
        int id = ventasNueva.cargarId();
        return id;
    }

    @Override
    public String[][] listarVentas(DefaultTableModel datos, long cuit, int codigoTipo) {
        String[][] ventas = ventasP.listarVentas(datos,cuit,codigoTipo);
        return ventas;
    }

    @Override
    public void modificarProducto(String nombre, String descripcion, String iva, String costo, String margen, String marca, String rubro) {
        modificarP.modificarProducto(nombre,descripcion,iva,costo,margen,marca,rubro);
    }

    @Override
    public ArrayList<String> rellenarCampos(String nombre) {
        ArrayList<String> campos = modificarP.rellenarCampos(nombre);
        return campos;
    }

    @Override
    public ArrayList<ArrayList<String>> cargarCombosM() {
        ArrayList<ArrayList<String>> combosM = modificarP.cargarCombosM();
        return combosM;
    }

    @Override
    public void cargarProducto(String nombre, String descripcion, String iva, String costo, String margen, String marca, String rubro, String cantidad) {
        nuevoProd.cargarProducto(nombre,descripcion,iva,costo,margen,marca,rubro,cantidad);
    }

    @Override
    public ArrayList<ArrayList<String>> cargarCombosP() {
        ArrayList<ArrayList<String>> combosP = nuevoProd.cargarCombos();
        return combosP;
    }

    @Override
    public boolean existe(String nombre) {
        boolean existeProducto = gestionProd.existe(nombre);
        return existeProducto;
    }

    @Override
    public ArrayList<String> llenarCombos(String tipo) {
        ArrayList<String> combosTipoG = gestionProd.llenarCombos(tipo);
        return combosTipoG;
    }

    @Override
    public void agregarColor(String color) {
        gestionProd.agregarColor(color);
    }

    @Override
    public void agregarMarca(String marca) {
        gestionProd.agregarMarca(marca);
    }

    @Override
    public void actualizarStock(String nombre, String cantidad, String talle, String color) {
        gestionProd.actualizarStock(nombre,cantidad,talle,color);
    }

    @Override
    public ArrayList<ArrayList<String>> cargarCombosStock() {
        ArrayList<ArrayList<String>> combosStock = gestionProd.cargarCombos();
        return combosStock;
    }

    @Override
    public String[][] listarTablaColor() {
        String[][] tablaColor = gestionProd.listarTablaColor();
        return tablaColor;
    }

    @Override
    public String[][] listarTablaMarca() {
        String[][] tablaMarcas = gestionProd.listarTablaMarca();
        return tablaMarcas;
    }

    @Override
    public ArrayList<String> agregarCaracteristicas(String nombre) {
        ArrayList<String> caracteristicas = gestionProd.agregarCaracteristicas(nombre);
        return caracteristicas;
    }

    @Override
    public String[][] listarCaracteristicas(String nombre) {
        String[][] lcaracteristicas = gestionProd.listarCaracteristicas(nombre);
        return lcaracteristicas;
    }

    @Override
    public boolean verificarExistencia(String nombre) {
        boolean existe = ventasNueva.verificarExistencia(nombre);
        return existe;
    }

    @Override
    public ArrayList<String> cargarProducto1(String nombre) {
        ArrayList<String> producto = ventasNueva.cargarProducto(nombre);
        return producto;
    }

    @Override
    public ArrayList<String> buscarCliente(String cuit) {
        ArrayList<String> cliente = ventasNueva.buscarCliente(cuit);
        return cliente;
    }

    @Override
    public int buscarCodColor(String color) {
        int codColor = ventasNueva.buscarCodColor(color);
        return codColor;
    }

    @Override
    public int buscarCodTalle(String talle) {
        int codTalle = ventasNueva.buscarCodTalle(talle);
        return codTalle;
    }

    @Override
    public int consultarStock(String nombre, int codColor, int codTalle) {
        int stock = ventasNueva.consultarStock(nombre,codColor,codTalle);
        return stock;
    }

    @Override
    public ArrayList<String> cargarLinea(String codigo, String descripcion, int cant, String talle, String color, String text, String text1, String text2, String id) {
        ArrayList<String> linea = ventasNueva.cargarLinea(codigo, descripcion, cant, talle, color, text, text1, text2, id);
        return linea;
    }

    @Override
    public void restaurarStock(String codigo, int cant, String talle, String color, int linea) {
        ventasNueva.restaurarStock(codigo,cant,talle,color,linea);
    }

    @Override
    public ArrayList<String> cargarCombosTalle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cargarLineaV(String fecha, int comprobante, double total, DefaultTableModel datos, String cliente) {
        ventasNueva.cargarLinea(fecha,comprobante,total,datos,cliente);
    }

    @Override
    public void cancelarVenta(DefaultTableModel datos, String id) {
        ventasNueva.cancelarVenta(datos,id);
    }

    @Override
    public void finalizarVenta(String id, String pago, String total) {
        ventasNueva.finalizarVenta(id,pago,total);
    }

    @Override
    public ArrayList<ArrayList<String>> cargarCombosV(String text) {
        ArrayList<ArrayList<String>> combosV = ventasNueva.cargarCombos(text);
        return combosV;
    }  
}
