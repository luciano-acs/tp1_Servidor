package Presentador;

import AFIP.AlicIva;
import AFIP.ArrayOfAlicIva;
import AFIP.ArrayOfFECAEDetRequest;
import AFIP.FEAuthRequest;
import AFIP.FECAECabRequest;
import AFIP.FECAEDetRequest;
import AFIP.FECAERequest;
import AFIP.FECAEResponse;
import AFIP.FERecuperaLastCbteResponse;
import AFIP.Service;
import AFIP.ServiceSoap;
import BD.ServidorBD;
import Modelo.ClienteT.Cliente;
import Modelo.Producto.ColorP;
import Modelo.Producto.Producto;
import Modelo.Producto.Talle;
import Modelo.Producto.TipoDeTalle;
import Modelo.Venta.Factura;
import Modelo.Venta.FormaDePago;
import Modelo.Venta.LineaDeVenta;
import Modelo.Venta.Pago;
import Modelo.Venta.Venta;
import Wrapper.Autorizacion;
import Wrapper.ILoginService;
import Wrapper.LoginService;
import static java.lang.Integer.parseInt;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorNuevaVenta{

    ServidorBD bd = new ServidorBD();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
    String GUID = "036F676C-6D35-4CEE-B315-DA9D55C43A10";

    public PresentadorNuevaVenta() {

    }

    public int cargarId() {
        int idVenta = bd.ultimoCod();    
        return idVenta;
    }

    public ArrayList<String> buscarCliente(String cuit) {
        ArrayList<String> cliente = new ArrayList<String>();
        Cliente cl = bd.buscarCliente(cuit);

        cliente.add(cl.getCuit());
        cliente.add(cl.getRazonSocial());        
        cliente.add(cl.getDomicilio());
        cliente.add(cl.getEmail());
        cliente.add(cl.getCondicion());
        
        return cliente;
    }

    public boolean verificarExistencia(String codigoProd) {
        boolean existe = false;
        if (bd.existe(codigoProd)) {
            if (bd.consultarStock(codigoProd) > 0) {
                existe = true;
            } else {
                showMessageDialog(null, "Producto sin stock");
            }
        } else {
            showMessageDialog(null, "El producto no exite");
            return false;
        }
        return existe;
    }

    public ArrayList<ArrayList<String>> cargarCombos(String cod) {
        ArrayList<ArrayList<String>> combos = new ArrayList<ArrayList<String>>();
        ArrayList<String> tipoV = new ArrayList<>();
        ArrayList<String> colorV = new ArrayList<>();
        
        ArrayList<TipoDeTalle> tipo = bd.listarStockTipoTalle(Integer.parseInt(cod));
        ArrayList<Modelo.Producto.ColorP> color = bd.listarStockColor(Integer.parseInt(cod));
        
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

    public ArrayList<String> cargarComboTalle(String tipo, String prod) {
        ArrayList<String> talles = new ArrayList<String>();

        ServidorBD bd = new ServidorBD();
        int cod = bd.buscarCodTipo(tipo);
        ArrayList<Talle> talle = bd.buscarStockTalle(cod, prod);

        for (int i = 0; i < talle.size(); i++) {
            talles.add(talle.get(i).getDescripcion());
        }
        return talles;
    }

    public int buscarCodColor(String color) {
        int codColor = 0;
        codColor = bd.buscarCodColor(color);
        return codColor;
    }

    public int buscarCodTalle(String talle) {
        int codTalle = 0;
        codTalle = bd.buscarCodTalle(talle);
        return codTalle;
    }

    public int consultarStock(String prod, int codColor, int codTalle) {
        int cantidadTotal = 0;
        cantidadTotal = bd.consultarStockParticular(prod, codColor, codTalle);
        return cantidadTotal;
    }

//    public void actualizarStock(int prod, int cant, int parseInt0, Talle t, ColorP co, double subtotal, double precio) {
//        bd.actualizarStock(prod, cant, parseInt(ventas.jtfID.getText()), t, co, subtotal, precio);
//    }


    public ArrayList<String> cargarProducto(String cod) {
        ArrayList<String> producto = new ArrayList<String>();
        Producto p = bd.listarProducto(Integer.parseInt(cod));

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

//    @Override
    public ArrayList<String> cargarLinea(String codigo, String descripcion, int cant, String talleD, String colorD, String costo, String porcIVA, String margen, String idVenta) {
        ArrayList<String> linea = new ArrayList<String>();
        Producto p = bd.buscarProducto(codigo);
        Talle t = new Talle(bd.buscarCodTalle(talleD), talleD);
        ColorP co = new ColorP(bd.buscarCodColor(colorD), colorD);

        double precio = p.calcularPrecio(Double.parseDouble(costo), Double.parseDouble(porcIVA), Double.parseDouble(margen));;
        double subtotal = cant * precio;

        linea.add(codigo);
        linea.add(descripcion);
        linea.add(""+cant);
        linea.add(""+precio);
        linea.add(""+subtotal);
        linea.add(""+t.getIdTalle());
        linea.add(""+co.getIdColor());

//        ""+parseInt(codigo)+""+cant+""+parseInt(ventas.jtfID.getText())+""+t+""+co+""+subtotal+""+precio
        bd.actualizarStock(parseInt(codigo), cant, parseInt(idVenta), t, co, subtotal, precio);
        return linea;
    }

    public void restaurarStock(String codigo, int cant, String codTalle, String codColor, int linea) {
        Talle t = new Talle(Integer.parseInt(codTalle), bd.buscarDTalle(Integer.parseInt(codTalle)));
        ColorP co = new ColorP(Integer.parseInt(codColor), bd.buscarDColor(Integer.parseInt(codColor)));

        bd.restaurarStock(codigo, cant, t, co, linea);
    }

    public void cargarLinea(String fecha, int comprobante, double total, DefaultTableModel datos, String cliente) {
        Cliente cl = bd.buscarCliente(cliente);

        ArrayList<LineaDeVenta> lista = new ArrayList<>();
        for (int i = 0; i < datos.getRowCount(); i++) {
            LineaDeVenta li = new LineaDeVenta();
            li.setCantidad(Integer.parseInt(datos.getValueAt(i, 2).toString()));

            Producto p = bd.buscarProducto(datos.getValueAt(i, 0).toString());
            li.setProducto(p);
            li.setPrecioU(Double.parseDouble(datos.getValueAt(i, 3).toString()));
            li.setSubtotal(Double.parseDouble(datos.getValueAt(i, 4).toString()));
            lista.add(li);
        }

        cargarVenta(fecha, comprobante, total, cl, lista);
    }

    private void cargarVenta(String fecha, int comprobante, double total, Cliente cl, ArrayList<LineaDeVenta> lista) {
        Venta ve = new Venta(fecha, comprobante, total, cl, lista);
        solicitarAutorizacion(ve);
    }

    private void solicitarAutorizacion(Venta ve) {
        FECAEResponse respuesta = autorizacionAFIP(ve);
        if (respuesta.getFeCabResp().getResultado().equals("A")) {
            bd.registrarVenta(ve, 25);

            showMessageDialog(null, "Venta registrada y aprobada");

            registrarFactura(ve, respuesta, "efectivo");
        } else {
            String mensaje1 = respuesta.getFeDetResp().getFECAEDetResponse().get(0).getObservaciones().getObs().get(0).getMsg();
            String mensaje2 = respuesta.getFeCabResp().getResultado();
            System.out.println(mensaje1);
            showMessageDialog(null, "La venta no fue autorizada: " + mensaje2);
        }
    }

    private FECAEResponse autorizacionAFIP(Venta ve) {
        int condTrib = 0;
        double sneto = 0;

        FEAuthRequest auth = generarAuth();

        //condicion tributaria
        if (ve.getCliente().getCondicion().equals("RI") || ve.getCliente().getCondicion().equals("M")) {
            condTrib = 1;
        }
        if (ve.getCliente().getCondicion().equals("E") || ve.getCliente().getCondicion().equals("CF") || ve.getCliente().getCondicion().equals("NR")) {
            condTrib = 6;
        }

        //neto e iva
        for (int i = 0; i < ve.getLista().size(); i++) {
            double costo = ve.getLista().get(i).getProducto().getCosto();
            double margen = ve.getLista().get(i).getProducto().getCosto() * ve.getLista().get(i).getProducto().getMargenGanancia();
            int cantidad = ve.getLista().get(i).getCantidad();
            double neto = (costo + margen) * cantidad;
            sneto = sneto + neto;
        }

        //afip
        Service servicioA = new Service();
        ServiceSoap loginA = servicioA.getServiceSoap();

        FERecuperaLastCbteResponse ultimo = loginA.feCompUltimoAutorizado(auth, 25, condTrib);

        FECAECabRequest feCabReq = new FECAECabRequest();
        feCabReq.setPtoVta(25); //constante
        feCabReq.setCbteTipo(condTrib); //variable
        feCabReq.setCantReg(1); //constante

        ArrayOfAlicIva iva = new ArrayOfAlicIva();
        AlicIva aliciva = new AlicIva();
        aliciva.setId(5); //constante
        aliciva.setBaseImp(sneto); //variable
        aliciva.setImporte(sneto * 0.21); //variable
        iva.getAlicIva().add(aliciva);

        ArrayOfFECAEDetRequest feDetReq = new ArrayOfFECAEDetRequest();

        FECAEDetRequest det = new FECAEDetRequest();

        det.setConcepto(1); //constante
        det.setDocTipo(80); //constante 
        det.setDocNro(Long.parseLong(ve.getCliente().getCuit()));
        det.setCbteDesde(ultimo.getCbteNro() + 1);
        det.setCbteHasta(ultimo.getCbteNro() + 1);
        det.setCbteFch(dtf.format(LocalDateTime.now()));
        det.setImpTotal(ve.getTotal());
        det.setImpNeto(sneto);
        det.setImpIVA(sneto * 0.21);
        det.setMonId("PES");
        det.setMonCotiz(1);
        det.setIva(iva);
        feDetReq.getFECAEDetRequest().add(det);

        FECAERequest feCAEReq = new FECAERequest();
        feCAEReq.setFeCabReq(feCabReq);
        feCAEReq.setFeDetReq(feDetReq);

        FECAEResponse solicitar = loginA.fecaeSolicitar(auth, feCAEReq);

        return solicitar;
    }

    public FEAuthRequest generarAuth() {
        //wrapper
        LoginService servicio = new LoginService();
        ILoginService login = servicio.getSGEAuthService();
        Autorizacion autorizacion = login.solicitarAutorizacion(GUID);

        FEAuthRequest auth = new FEAuthRequest();
        auth.setCuit(autorizacion.getCuit());
        auth.setSign(autorizacion.getSign().getValue());
        auth.setToken(autorizacion.getToken().getValue());
        String error = autorizacion.getError().getValue();

        return auth;
    }

    private void registrarFactura(Venta ve, FECAEResponse respuesta, String efectivo) {
        Factura fact = new Factura(Math.toIntExact(respuesta.getFeDetResp().getFECAEDetResponse().get(0).getCbteDesde()),
                respuesta.getFeDetResp().getFECAEDetResponse().get(0).getCbteFch(),
                ve.getTotal(), ve);

        bd.registarFacturaAfip(fact.getNumFact(), respuesta.getFeDetResp().getFECAEDetResponse().get(0).getCAE(),
                respuesta.getFeDetResp().getFECAEDetResponse().get(0).getCAEFchVto(),
                efectivo);

        bd.registrarFactura(fact);
    }

    public void cancelarVenta(DefaultTableModel datos, String linea) {

        for (int i = 0; i < datos.getRowCount(); i++) {
            String codigo = (String) datos.getValueAt(i, 0);
            int cant = (int) datos.getValueAt(i, 2);
            Talle t = new Talle((int) datos.getValueAt(i, 5), bd.buscarDTalle((int) datos.getValueAt(i, 5)));
            ColorP co = new ColorP((int) datos.getValueAt(i, 6), bd.buscarDColor((int) datos.getValueAt(i, 6)));

            bd.restaurarStock(codigo, cant, t, co, Integer.parseInt(linea));
        }
        datos.setRowCount(0);
        for (int i = 0; i < datos.getRowCount(); i++) {
            datos.removeRow(i);
        }
        showMessageDialog(null, "Venta cancelada");
    }

    public void finalizarVenta(String id, String tipopago, String total) {
        Venta ve = bd.buscarVenta(Integer.parseInt(id));

        FormaDePago fdp = bd.buscarFormaPago(tipopago);
        Pago pago = new Pago(Integer.parseInt(id),
                 Double.parseDouble(total), fdp, ve);
        bd.registrarPago(pago);;
    }

}
