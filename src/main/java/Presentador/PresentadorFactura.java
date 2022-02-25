/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentador;

import AFIP.FECAEResponse;
import Modelo.Organizacion.Afip;
import Modelo.Producto.Producto;
import Modelo.Venta.Factura;
import Modelo.Venta.Venta;
import Vista.pFacturas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorFactura implements ActionListener, Printable{
    
    pFacturas factura = new pFacturas();
    Producto p = new Producto();
    
    PresentadorFactura(pFacturas fact, Venta ve, FECAEResponse respuesta, String tipo) {
        double sneto = 0;
        String tipoFact = "";
        this.factura = fact;
        factura.btnImprimir.addActionListener(this);
        factura.btnSalir.addActionListener(this);
        
        if(ve.getCliente().getCondicion().equals("RI")||ve.getCliente().getCondicion().equals("M")){
            tipoFact = "A";
        }else{
            tipoFact = "B";
        }
        
        for(int i=0; i<ve.getLista().size();i++){
            double costo = ve.getLista().get(i).getProducto().getCosto();
            double margen = ve.getLista().get(i).getProducto().getCosto()*ve.getLista().get(i).getProducto().getMargenGanancia();
            int cantidad = ve.getLista().get(i).getCantidad();
            double neto = (costo+margen)*cantidad;
            sneto = sneto+neto;
        }
        
        factura.jlEmpresa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/percha (3).png")));
        factura.jlTipo.setText(tipoFact); //if
        factura.jlNroFact.setText(""+respuesta.getFeDetResp().getFECAEDetResponse().get(0).getCbteHasta());
        factura.jlFecha.setText(respuesta.getFeDetResp().getFECAEDetResponse().get(0).getCbteFch()); 
        factura.jlCUITEmp.setText(""+respuesta.getFeCabResp().getCuit());
        factura.jlRazon.setText(ve.getCliente().getRazonSocial());
        factura.jlCUITCl.setText(ve.getCliente().getCuit());
        factura.jlCondIVA.setText(ve.getCliente().getCondicion()); //if
        factura.jlCondVenta.setText(tipo);
        factura.jlBase.setText(""+sneto);
        factura.jlIVA.setText(""+sneto*0.21);
        factura.jlTotal.setText(""+ve.getTotal());
        factura.jlCAE.setText(respuesta.getFeDetResp().getFECAEDetResponse().get(0).getCAE());
        factura.jlVtoCAE.setText(respuesta.getFeDetResp().getFECAEDetResponse().get(0).getCAEFchVto());
        
        DefaultTableModel datos = (DefaultTableModel) factura.jtLinea.getModel();        
        
        for(int i=0;i<ve.getLista().size();i++){
            double precio = p.calcularPrecio(ve.getLista().get(i).getProducto().getCosto(),
                                             ve.getLista().get(i).getProducto().getPorcIVA(),
                                             ve.getLista().get(i).getProducto().getMargenGanancia());
            double subtotal = ve.getLista().get(i).getCantidad() * precio;
            Object[] fila = {ve.getLista().get(i).getProducto().getCodigo(),
                             ve.getLista().get(i).getProducto().getDescripcion(),
                             ve.getLista().get(i).getCantidad(),
                             precio,
                             "21%",
                             subtotal
                             };
            datos.addRow(fila);
        }
    }
    
    PresentadorFactura(pFacturas fact) {
        this.factura = fact;
        factura.btnImprimir.addActionListener(this);
        factura.btnSalir.addActionListener(this);
    }

    PresentadorFactura(pFacturas factura, ArrayList<Venta> ve, Afip afip, Factura fact) {
        factura.btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/imprimir-contorno-del-boton.png")));
        factura.btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png")));
        
        double sneto = 0;
        String tipoFact;
        this.factura = factura;
        factura.btnImprimir.addActionListener(this);
        factura.btnSalir.addActionListener(this);
        
        if(ve.get(0).getCliente().getCondicion().equals("RI")||ve.get(0).getCliente().getCondicion().equals("M")){
            tipoFact = "A";
        }else{
            tipoFact = "B";
        }
        
        
        for(int i=0; i<ve.get(0).getLista().size();i++){
            double costo = ve.get(0).getLista().get(i).getProducto().getCosto();
            double margen = ve.get(0).getLista().get(i).getProducto().getCosto()*ve.get(0).getLista().get(i).getProducto().getMargenGanancia();
            int cantidad = ve.get(0).getLista().get(i).getCantidad();
            double neto = (costo+margen)*cantidad;
            sneto = sneto+neto;
        }
        
        
        factura.jlEmpresa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/percha (3).png")));
        factura.jlTipo.setText(tipoFact); //if
        factura.jlNroFact.setText(""+fact.getNumFact());
        factura.jlFecha.setText(fact.getFecha()); 
        factura.jlCUITEmp.setText("20000000001");
        factura.jlRazon.setText(ve.get(0).getCliente().getRazonSocial());
        factura.jlCUITCl.setText(ve.get(0).getCliente().getCuit());
        factura.jlCondIVA.setText(ve.get(0).getCliente().getCondicion()); //if
        factura.jlCondVenta.setText(afip.getCondicion());
        factura.jlBase.setText(""+sneto);
        factura.jlIVA.setText(""+sneto*0.21);
        factura.jlTotal.setText(""+ve.get(0).getTotal());
        factura.jlCAE.setText(afip.getCAE());
        factura.jlVtoCAE.setText(afip.getVtoCAE());
        
        DefaultTableModel datos = (DefaultTableModel) factura.jtLinea.getModel();        
        datos.setNumRows(0);
        for(int i=0;i<ve.get(0).getLista().size();i++){
            double precio = p.calcularPrecio(ve.get(0).getLista().get(i).getProducto().getCosto(),
                                             ve.get(0).getLista().get(i).getProducto().getPorcIVA(),
                                             ve.get(0).getLista().get(i).getProducto().getMargenGanancia());
            double subtotal = ve.get(0).getLista().get(i).getCantidad() * precio;
            Object[] fila = {ve.get(0).getLista().get(i).getProducto().getCodigo(),
                             ve.get(0).getLista().get(i).getProducto().getDescripcion(),
                             ve.get(0).getLista().get(i).getCantidad(),
                             precio,
                             "21%",
                             subtotal
                             };
            datos.addRow(fila);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(factura.btnSalir)){
            limpiar();
            factura.dispose();
        }
        if(e.getSource().equals(factura.btnImprimir)){
            PrinterJob gap = PrinterJob.getPrinterJob();
            gap.setPrintable(this);
            boolean top = gap.printDialog();
            if(top){
                try{
                    gap.print();
                } catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(null,"No se pudo imprimir");
                }
            }
            limpiar();
            factura.dispose();
        }
        e.setSource("");
    }
    
    public void limpiar(){
        factura.jlTipo.setText(""); //if
        factura.jlNroFact.setText("");
        factura.jlFecha.setText(""); 
        factura.jlCUITEmp.setText("");
        factura.jlRazon.setText("");
        factura.jlCUITCl.setText("");
        factura.jlCondIVA.setText(""); //if
        factura.jlCondVenta.setText("");
        factura.jlBase.setText("");
        factura.jlIVA.setText("");
        factura.jlTotal.setText("");
        factura.jlCAE.setText("");
        factura.jlVtoCAE.setText("");
        
        DefaultTableModel datos = (DefaultTableModel) factura.jtLinea.getModel();
        for(int i=0;i<factura.jtLinea.getRowCount();i++){
            datos.removeRow(i);
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if(pageIndex>0){
            return NO_SUCH_PAGE;
        }
        Graphics2D hub = (Graphics2D) graphics;
        hub.translate(pageFormat.getImageableX() + 10, pageFormat.getImageableY() + 10);
        hub.scale(0.85,0.85);
        factura.jPanel1.print(graphics);
        return PAGE_EXISTS;
    }

    
}
