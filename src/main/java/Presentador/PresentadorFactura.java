/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentador;

import AFIP.FECAEResponse;
import BD.ServidorBD;
import Modelo.Organizacion.Afip;
import Modelo.Producto.Producto;
import Modelo.Venta.Factura;
import Modelo.Venta.Venta;
import java.util.ArrayList;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorFactura {

    Producto p = new Producto();

    ServidorBD bd = new ServidorBD();

    public PresentadorFactura() {

    }

    public ArrayList<String> buscarFactura(String id, String cuit) {
        String tipoFact = "";
        Double sneto = 0.0;
        
        ArrayList<Venta> ve = bd.busquedaVenta(Long.parseLong(cuit), "a.idVenta", Integer.parseInt(id));
        Factura fact = bd.buscarFactura(Integer.parseInt(id));
        Afip afip = bd.buscarFacturaAfip(fact.getNumFact());
        
        ArrayList<String> factura = new ArrayList<String>();        
        
        if (ve.get(0).getCliente().getCondicion().equals("RI") || ve.get(0).getCliente().getCondicion().equals("M")) {
            tipoFact = "A";
        } else {
            tipoFact = "B";
        }
        
        for (int i = 0; i < ve.get(0).getLista().size(); i++) {
            double costo = ve.get(0).getLista().get(i).getProducto().getCosto();
            double margen = ve.get(0).getLista().get(i).getProducto().getCosto() * ve.get(0).getLista().get(i).getProducto().getMargenGanancia();
            int cantidad = ve.get(0).getLista().get(i).getCantidad();
            double neto = (costo + margen) * cantidad;
            sneto = sneto + neto;
        }
        
        factura.add(0, tipoFact);
        factura.add(1, ""+fact.getNumFact());
        factura.add(2, afip.getVtoCAE());
        factura.add(3,"20000000001");
        factura.add(4, ve.get(0).getCliente().getRazonSocial());
        factura.add(5, ve.get(0).getCliente().getCuit());
        factura.add(6, ve.get(0).getCliente().getCondicion());
        factura.add(7, "efectivo");
        factura.add(8, "" + sneto);
        factura.add(9, "" + sneto * 0.21);
        factura.add(10, "" + ve.get(0).getTotal());
        factura.add(11, afip.getCAE());
        factura.add(12, afip.getVtoCAE());
        
        return factura;
    }
}
