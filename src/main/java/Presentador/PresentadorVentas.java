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
import Servidor.ServidorBD;
import Modelo.ClienteT.Cliente;
import Modelo.Organizacion.Afip;
import Modelo.Producto.*;
import Modelo.Venta.*;
import Vista.*;
import Wrapper.Autorizacion;
import Wrapper.ILoginService;
import Wrapper.LoginService;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import static java.awt.event.ItemEvent.SELECTED;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorVentas implements ActionListener, java.awt.event.ItemListener{

    vistaMenu menu = new vistaMenu();
    pVentas ventas = new pVentas();
    pFacturas factura = new pFacturas();
    Venta v = new Venta();
    ServidorBD bd = new ServidorBD();
    nvoCliente nvoCliente = new nvoCliente();
    pDevoluciones devoluciones = new pDevoluciones();
    pListarVentas listaVentas = new pListarVentas();
    
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    String GUID = "036F676C-6D35-4CEE-B315-DA9D55C43A10";
    
    public PresentadorVentas(pVentas vista, vistaMenu menu, double saldo) {
        this.ventas = vista;
        this.menu = menu;
        
        ventas.jtfMonto.setText(""+0);
        ventas.jtfMonto.setEnabled(false);
        ventas.jlSaldo.setText(""+saldo);
        ventas.jlSaldo.setVisible(false);
        ventas.btnBuscarCliente.addActionListener(this);
        ventas.btnBuscarP.addActionListener(this);
        ventas.btnConfirmar.addActionListener(this);
        ventas.btnEliminar.addActionListener(this);
        ventas.cbTipo.addItemListener(this);
        ventas.cbPago.addItemListener(this);
        ventas.btnCancelarVenta.addActionListener(this);
        ventas.btnRegistarVenta.addActionListener(this);
        ventas.btnFinalizarVenta.addActionListener(this);
        
        ventas.jlTotal.setText(""+0);
        ventas.cbColor.removeAllItems();
        ventas.cbPago.removeAllItems();
        ventas.cbTalle.removeAllItems();
        ventas.cbTipo.removeAllItems();
        ventas.jtfCUIT.setText("");
        ventas.jtfDescripcion.setText("");
        ventas.jtfCVV.setText("");
        ventas.jtfCantidad.setText("");
        ventas.jtfMarca.setText("");
        ventas.jtfNombre.setText("");
        ventas.jtfRazonSocial.setText("");
        ventas.jtfTarjeta.setText("");
        ventas.jtfVto.setText("");
        ventas.jtLinea.getRowCount();
            
        DefaultTableModel datos = (DefaultTableModel) ventas.jtLinea.getModel();
        System.out.println(ventas.jtLinea.getRowCount());
        for(int i = 0;i<ventas.jtLinea.getRowCount();i++){
            datos.removeRow(i);
            i-=1;
        }
        
        ventas.jtfMonto.setEnabled(false);
        ventas.cbPago.setEnabled(true);
        ventas.jtfCVV.setEnabled(false);
        ventas.jtfTarjeta.setEnabled(false);
        ventas.jtfVto.setEnabled(false);   
        
        factura.btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/imprimir-contorno-del-boton.png")));
        factura.btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png")));
    } 

    PresentadorVentas() {
        
    }

    PresentadorVentas(pDevoluciones devolucion, pVentas vista, vistaMenu menu) {
        this.devoluciones = devolucion;
        this.ventas = vista;
        this.menu = menu;
        
        devoluciones.btnConsultarVenta.addActionListener(this);
        devoluciones.btnNuevaVenta.addActionListener(this);
//        devoluciones.btnReintegrar.addActionListener(this);
        devoluciones.btnReintegroTotal.addActionListener(this);
        
        devoluciones.jtfCUIT.setText("");
//        devoluciones.jtfCantidadR.setText("");
        devoluciones.jtfFactura.setText("");
        devoluciones.jtfSaldo.setText("");
        devoluciones.btnReintegroTotal.setEnabled(true);

    }

    PresentadorVentas(pListarVentas lista) {
        this.listaVentas = lista;
        listaVentas.btnBuscarV.addActionListener(this);
        listaVentas.btnMostrarDetalles.addActionListener(this);
        listaVentas.cbTipoBusq.addItemListener(this);
        
        listaVentas.jtfCodigo.setText("");
        listaVentas.jtfCuit.setText("");
        listaVentas.jLabel5.setText("");
        
        DefaultTableModel datos = (DefaultTableModel) listaVentas.jtVentas.getModel();
        for(int i = 0;i<datos.getRowCount();i++){
            datos.removeRow(i);
            i-=1;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(ventas.btnBuscarCliente)){
            buscarCliente();
            Cliente cl = buscarCliente();   
            if(cl.getCuit()==null){
                int resp = JOptionPane.showConfirmDialog(null,"Desea registrar un nuevo clientes?");
                if(JOptionPane.OK_OPTION==resp){
                    PresentadorClientes pc = new PresentadorClientes(nvoCliente);
                    pc.cargarCombo();
                    nvoCliente.setVisible(true);
                }
            }
        }
        if(e.getSource().equals(ventas.btnBuscarP)){
            if(ventas.jtfNombre.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Ingrese producto a vender");
            }else{
                if(bd.existe(ventas.jtfNombre.getText())){
                    if(bd.consultarStock(ventas.jtfNombre.getText()) > 0){
                        buscarProducto();
                    }else{
                        showMessageDialog(null, "Producto sin stock");
                    }
                }else{
                    ventas.jtfNombre.setText("");
                    showMessageDialog(null, "El producto no exite");
                }
            }
        }
        if(e.getSource().equals(ventas.btnConfirmar)){
            if((ventas.jtfCantidad.getText().isEmpty()&&ventas.jtfDescripcion.getText().isEmpty())||ventas.jtfCantidad.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Ingrese cantidad");
            }else{
                int codColor = bd.buscarCodColor(ventas.cbColor.getSelectedItem().toString());
                int codTalle = bd.buscarCodTalle(ventas.cbTalle.getSelectedItem().toString());
                if(Integer.parseInt(ventas.jtfCantidad.getText()) <= bd.consultarStockParticular(ventas.jtfNombre.getText(),codColor,codTalle)){
                
                    String codigo = ventas.jtfNombre.getText();
                    String descripcion = ventas.jtfDescripcion.getText();
                    int cant = parseInt(ventas.jtfCantidad.getText());
            
                    Producto p = bd.buscarProducto(codigo);    
                    Talle t = new Talle(bd.buscarCodTalle(ventas.cbTalle.getSelectedItem().toString()),ventas.cbTalle.getSelectedItem().toString());
                    ColorP co = new ColorP(bd.buscarCodColor(ventas.cbColor.getSelectedItem().toString()),ventas.cbColor.getSelectedItem().toString());
            
                    double precio = p.calcularPrecio(p.getCosto(), p.getPorcIVA(), p.getMargenGanancia());;
                    double subtotal = cant * precio;            
            
                    DefaultTableModel datos = (DefaultTableModel) ventas.jtLinea.getModel();        
                    Object[] fila = {codigo,descripcion,cant,precio,subtotal,t.getIdTalle(),co.getIdColor()};                
                    datos.addRow(fila); 
            
                    total();
                    bd.actualizarStock(parseInt(codigo),cant,parseInt(ventas.jtfID.getText()),t,co,subtotal,precio);                    
                
                }else{
                    showMessageDialog(null, "Stock no disponible. En stock para dicho talle y color son: "+bd.consultarStockParticular(ventas.jtfNombre.getText(),codColor,codTalle));
                }
            }
        }
        if(e.getSource().equals(ventas.btnEliminar)){
            DefaultTableModel datos = (DefaultTableModel) ventas.jtLinea.getModel();
            if(datos.getRowCount()!=0){
                int fila = ventas.jtLinea.getSelectedRow();
                if(fila==-1){
                    JOptionPane.showMessageDialog(null,"Ninguna fila seleccionada");
                }else{
                    int linea = parseInt(ventas.jtfID.getText());
            
                    String codigo = (String) ventas.jtLinea.getValueAt(fila, 0);
                    int cant = (int) ventas.jtLinea.getValueAt(fila, 2);
                    Talle t = new Talle((int) ventas.jtLinea.getValueAt(fila, 5),bd.buscarDTalle((int) ventas.jtLinea.getValueAt(fila, 5)));
                    ColorP co = new ColorP((int) ventas.jtLinea.getValueAt(fila, 6),bd.buscarDColor((int) ventas.jtLinea.getValueAt(fila, 6)));
            
                    bd.restaurarStock(codigo,cant,t,co,linea);            
            
                    datos.removeRow(fila);
                    total();
                }
            }
        }
        if(e.getSource().equals(ventas.btnRegistarVenta)){
            double montoFinal = Double.parseDouble(ventas.jlTotal.getText())-Double.parseDouble(ventas.jlSaldo.getText());
            if(ventas.jtfNombre.getText().isEmpty()||ventas.jtfRazonSocial.getText().equals("")||
                    montoFinal<0){
                JOptionPane.showMessageDialog(null,"Campos sin completar o monto erroneo");
            }else{
                ventas.btnCancelarVenta.setEnabled(false);
                cargarVenta();
            }
        }
        if(e.getSource().equals(ventas.btnCancelarVenta)){
            cancelarVenta();            
        }
        if(e.getSource().equals(ventas.btnFinalizarVenta)){
            if(ventas.jtfNombre.getText().equals("")&&ventas.jtfRazonSocial.getText().equals("")&&
                    ventas.jtfMonto.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Campos sin completar");
            }else{
                Venta ve = bd.buscarVenta(Integer.parseInt(ventas.jtfID.getText()));
            
                FormaDePago fdp = bd.buscarFormaPago(ventas.cbPago.getSelectedItem().toString());
                Pago pago = new Pago(Integer.parseInt(ventas.jtfID.getText())
                                 ,Double.parseDouble(ventas.jlTotal.getText()),fdp,ve);
                bd.registrarPago(pago);
            
                PresentadorFactura pf = new PresentadorFactura(factura);           
            
                factura.setVisible(true);
                cardarId();
            }
        }
        if(e.getSource().equals(listaVentas.btnBuscarV)){
            if("".equals(listaVentas.jtfCodigo.getText())||"".equals(listaVentas.jtfCuit.getText())){
                JOptionPane.showMessageDialog(null,"Campos sin completar");
            }else{
                String tipo = "";
            
                if(listaVentas.jlTipoBusq.getText().equals("Id Venta")){
                    tipo = "a.idVenta";
                } 
                if(listaVentas.jlTipoBusq.getText().equals("Num Factura")){
                    tipo = "b.numFactura";
                }
                if(listaVentas.jlTipoBusq.getText().equals("Fecha")){
                    tipo = "a.fecha";
                }
            
                long cuit = Long.parseLong(listaVentas.jtfCuit.getText());
                int codigoTipo = Integer.parseInt(listaVentas.jtfCodigo.getText());
                ArrayList<Venta> ve = bd.busquedaVenta(cuit,tipo,codigoTipo);
            
                if(ve.isEmpty()){
                    JOptionPane.showMessageDialog(listaVentas, "No existe venta");
                }else{
                    listarVentas(ve);
                }
            }
        }
        if(e.getSource().equals(listaVentas.btnMostrarDetalles)){
            int fila = listaVentas.jtVentas.getSelectedRow();
            if(fila==-1){
                JOptionPane.showMessageDialog(null, "Ninguna fila seleccionada");
            }else{
                long cuit = Long.parseLong(listaVentas.jtfCuit.getText());
            
                DefaultTableModel datos = (DefaultTableModel) listaVentas.jtVentas.getModel();
                int codigo = Integer.parseInt(datos.getValueAt(fila, 0).toString());
            
                ArrayList<Venta> venta = bd.busquedaVenta(cuit,"a.idVenta",codigo);
                Factura fact = bd.buscarFactura(codigo);
                Afip afip = bd.buscarFacturaAfip(fact.getNumFact());
                PresentadorFactura pf = new PresentadorFactura(factura,venta,afip,fact);
                factura.setVisible(true);
            }
        }
        if(e.getSource().equals(devoluciones.btnNuevaVenta)){
            menu.btnDevoluciones.setBackground(Color.BLACK);            
            menu.btnDevoluciones.setForeground(Color.white);
            menu.btnVentas.setBackground(Color.WHITE);
            menu.btnVentas.setForeground(Color.black);
            
            devoluciones.setVisible(false);
            ventas.setVisible(true);
            
            double saldo = Double.parseDouble(devoluciones.jtfSaldo.getText());
            PresentadorVentas pv = new PresentadorVentas(ventas,menu,saldo);
            cardarId();
        }
        if(e.getSource().equals(devoluciones.btnConsultarVenta)){
            if(devoluciones.jtfCUIT.getText().isEmpty()||devoluciones.jtfFactura.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Campos sin completar");
            }else{
                ArrayList<Venta> venta = bd.busquedaVenta(Long.parseLong(devoluciones.jtfCUIT.getText()),"b.numFactura",Integer.parseInt(devoluciones.jtfFactura.getText()));
                listarLineasDeVenta(venta);
            }
        }
        if(e.getSource().equals(devoluciones.btnReintegroTotal)){
            DefaultTableModel datos = (DefaultTableModel) devoluciones.jtLinea.getModel();
            if(datos.getRowCount()==0){
                JOptionPane.showMessageDialog(null,"No hay venta seleccionada");
            }else{
                double saldoTotal=0;
                for(int i=0;i<datos.getRowCount();i++){
                    String codigo = devoluciones.jtLinea.getValueAt(i, 0).toString();
                    int cant = (int) devoluciones.jtLinea.getValueAt(i, 2);
                    Talle t = new Talle(bd.buscarCodTalle((String)devoluciones.jtLinea.getValueAt(i, 5)),(String)devoluciones.jtLinea.getValueAt(i, 5));
                    ColorP co = new ColorP(bd.buscarCodColor((String)devoluciones.jtLinea.getValueAt(i, 6)),(String)devoluciones.jtLinea.getValueAt(i, 6));
                    saldoTotal = saldoTotal + (double)devoluciones.jtLinea.getValueAt(i, 4);
                    bd.actualizarLinea((int)datos.getValueAt(i, 7),cant);
                    bd.restaurarStockL(codigo,cant,t,co);                    
                }
                bd.actualizarVenta(Integer.parseInt(datos.getValueAt(0,8).toString()));
                    datos.setRowCount(0);
                    for(int i=0;i<datos.getRowCount();i++){
                        datos.removeRow(i);
                    }
                    
                    
                    devoluciones.jtfSaldo.setText(""+saldoTotal);
                    devoluciones.btnReintegroTotal.setEnabled(false);
                    showMessageDialog(null, "Venta Reintegrada");
            }
        }
        e.setSource("A");
    }
    
    private void total() {
        if(ventas.jtLinea.getRowCount()>0){
            double sum = 0;
            for(int i=0;i<ventas.jtLinea.getRowCount();i++){
                sum += (double) ventas.jtLinea.getValueAt(i, 4);
                ventas.jlTotal.setText(""+sum);
            }
        }else{
            ventas.jlTotal.setText(""+0);
        }
    }
//    
    private void cargarVenta() {        
        
        String fecha = dtf.format(LocalDateTime.now());
        int comprobante = parseInt(ventas.jtfID.getText());
        double total = parseDouble(ventas.jlTotal.getText());
        Cliente cl = bd.buscarCliente(ventas.jtfCUIT.getText());        
        
        ArrayList<LineaDeVenta> lista = new ArrayList<>();
        DefaultTableModel datos = (DefaultTableModel) ventas.jtLinea.getModel();  
        for(int i = 0;i<ventas.jtLinea.getRowCount();i++){
            LineaDeVenta li = new LineaDeVenta();
            li.setCantidad((int) datos.getValueAt(i, 2));
            
            Producto p = bd.buscarProducto(ventas.jtLinea.getValueAt(i, 0).toString());

            li.setProducto(p);
            li.setPrecioU((double)datos.getValueAt(i, 3));
            li.setSubtotal(Double.parseDouble(datos.getValueAt(i, 4).toString()));
            lista.add(li);
        }                   
            
        Venta ve = new Venta(fecha,comprobante,total,cl,lista);
        FECAEResponse respuesta = autorizacionAFIP(ve);
        
        if(respuesta.getFeCabResp().getResultado().equals("A")){
            bd.registrarVenta(ve,25);        
        
            double monto = Double.parseDouble(ventas.jlTotal.getText())-Double.parseDouble(ventas.jlSaldo.getText());
            ventas.jtfMonto.setText(""+monto);
            
            datos.setRowCount(0);
            for(int i=0;i<datos.getRowCount();i++){
                datos.removeRow(i);
            }
            
            showMessageDialog(null,"Venta registrada y aprobada");
            
            
            ArrayList<FormaDePago> formas = bd.listarFormas();
            for(int i = 0;i<formas.size();i++){
                ventas.cbPago.addItem(formas.get(i).getDescripcion());
            }
            ventas.cbPago.setEnabled(true);
            
            PresentadorFactura pf = new PresentadorFactura(factura,ve,respuesta,ventas.cbPago.getSelectedItem().toString());
            Factura fact = new Factura(Math.toIntExact(respuesta.getFeDetResp().getFECAEDetResponse().get(0).getCbteDesde()),
                                       respuesta.getFeDetResp().getFECAEDetResponse().get(0).getCbteFch(),
                                       ve.getTotal(),ve);
            
            bd.registarFacturaAfip(fact.getNumFact(),respuesta.getFeDetResp().getFECAEDetResponse().get(0).getCAE(),
                                    respuesta.getFeDetResp().getFECAEDetResponse().get(0).getCAEFchVto(),
                                    ventas.cbPago.getSelectedItem().toString());
            bd.registrarFactura(fact);
        }else{
            cancelarVenta();
            String mensaje1 = respuesta.getFeDetResp().getFECAEDetResponse().get(0).getObservaciones().getObs().get(0).getMsg();
            String mensaje2 = respuesta.getFeCabResp().getResultado();
            System.out.println(mensaje1);
            showMessageDialog(null, "La venta no fue autorizada: "+mensaje2);
        }
    }   

    public void cardarId() {
        
        int idVenta = bd.ultimoCod();
        ventas.jtfID.setHorizontalAlignment(SwingConstants.CENTER);
        ventas.jtfID.setText(""+idVenta);
        ventas.jtfID.setEnabled(false);
    }

    private Cliente buscarCliente() {
        String cuit = ventas.jtfCUIT.getText();
        Cliente c = bd.buscarCliente(cuit);
        
        ventas.jtfRazonSocial.setText(c.getRazonSocial());
        return c;
    }

    private void buscarProducto() {
        int cod = parseInt(ventas.jtfNombre.getText());
        Producto p = bd.listarProducto(cod);

        ventas.jtfDescripcion.setText(p.getDescripcion());
        ventas.jtfMarca.setText(p.getMarca().getDescripcionM());
        ventas.cbColor.removeAllItems();
        ventas.cbTipo.removeAllItems();
        ventas.cbTalle.removeAllItems();
        
        ArrayList<TipoDeTalle> tipo = bd.listarStockTipoTalle(cod);
        ArrayList<Modelo.Producto.ColorP> color = bd.listarStockColor(cod);
        
        ventas.cbTipo.setEnabled(true);
        for(int i =0;i<tipo.size();i++){
            if(tipo.get(i) != null){
                ventas.cbTipo.addItem(tipo.get(i).getDescripcion());
            }else{
                ventas.cbTipo.setEnabled(false);
            }
            
        }
        ventas.cbColor.setEnabled(true);
        for(int i =0;i<color.size();i++){
            if(color.get(i)!=null){
                ventas.cbColor.addItem(color.get(i).getDescripcion());
            }else{
                ventas.cbColor.setEnabled(false);
            }            
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange()==SELECTED){
            ventas.cbTalle.removeAllItems();
            if(ventas.cbTipo.getSelectedIndex()>-1){
                int cod = bd.buscarCodTipo(ventas.cbTipo.getSelectedItem().toString());        
                ArrayList<Talle> talle =  bd.buscarStockTalle(cod,ventas.jtfNombre.getText());
                ventas.cbTalle.setEnabled(true);
                for(int i =0;i<talle.size();i++){
                    if(talle.get(i)!=null){
                        ventas.cbTalle.addItem(talle.get(i).getDescripcion());
                    }else{
                        ventas.cbTalle.setEnabled(false);
                    }
                                
                }
            }
        }
        if(e.getStateChange()==SELECTED){
            if(ventas.cbPago.getSelectedIndex()>-1){
                if(ventas.cbPago.getSelectedItem().equals("tarjeta")){
                    ventas.jtfTarjeta.setEnabled(true);
                    ventas.jtfVto.setEnabled(true);
                    ventas.jtfCVV.setEnabled(true);
                }else{
                    ventas.jtfTarjeta.setEnabled(false);
                    ventas.jtfVto.setEnabled(false);
                    ventas.jtfCVV.setEnabled(false);
                }
            }
        }
        if(e.getStateChange()==SELECTED){
            if(listaVentas.cbTipoBusq.getSelectedIndex()>-1){
                if(listaVentas.cbTipoBusq.getSelectedItem().equals("Id Venta")){
                    listaVentas.jlTipoBusq.setText("Id Venta");
                }
                if(listaVentas.cbTipoBusq.getSelectedItem().equals("Num Factura")){
                    listaVentas.jlTipoBusq.setText("Num Factura");
                }
                if(listaVentas.cbTipoBusq.getSelectedItem().equals("Fecha")){
                    listaVentas.jlTipoBusq.setText("Fecha");
                }                
            }
        }
    }

    private FECAEResponse autorizacionAFIP(Venta ve) {
        int condTrib = 0;
        double sneto = 0;
        
        FEAuthRequest auth = generarAuth();
                
        //condicion tributaria
        if(ve.getCliente().getCondicion().equals("RI")||ve.getCliente().getCondicion().equals("M")){
            condTrib = 1;
        }
        if(ve.getCliente().getCondicion().equals("E")||ve.getCliente().getCondicion().equals("CF")||ve.getCliente().getCondicion().equals("NR")){
            condTrib = 6;
        }
        
        //neto e iva
        for(int i=0; i<ve.getLista().size();i++){
            double costo = ve.getLista().get(i).getProducto().getCosto();
            double margen = ve.getLista().get(i).getProducto().getCosto()*ve.getLista().get(i).getProducto().getMargenGanancia();
            int cantidad = ve.getLista().get(i).getCantidad();
            double neto = (costo+margen)*cantidad;
            sneto = sneto+neto;
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
        aliciva.setImporte(sneto*0.21); //variable
        iva.getAlicIva().add(aliciva);
        
        ArrayOfFECAEDetRequest feDetReq = new ArrayOfFECAEDetRequest();
        
        FECAEDetRequest det = new FECAEDetRequest();
        
        det.setConcepto(1); //constante
        det.setDocTipo(80); //constante 
        det.setDocNro(Long.parseLong(ve.getCliente().getCuit())); 
        det.setCbteDesde(ultimo.getCbteNro()+1);
        det.setCbteHasta(ultimo.getCbteNro()+1);
        det.setCbteFch(dtf.format(LocalDateTime.now()));
        det.setImpTotal(ve.getTotal());
        det.setImpNeto(sneto);
        det.setImpIVA(sneto*0.21);
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

    private void cancelarVenta() {
        DefaultTableModel datos = (DefaultTableModel) ventas.jtLinea.getModel();
            int linea = parseInt(ventas.jtfID.getText());
            
            for(int i=0;i<datos.getRowCount();i++){
                String codigo = (String) ventas.jtLinea.getValueAt(i, 0);
                int cant = (int) ventas.jtLinea.getValueAt(i, 2);
                Talle t = new Talle((int) ventas.jtLinea.getValueAt(i, 5),bd.buscarDTalle((int) ventas.jtLinea.getValueAt(i, 5)));
                ColorP co = new ColorP((int) ventas.jtLinea.getValueAt(i, 6),bd.buscarDColor((int) ventas.jtLinea.getValueAt(i, 6)));
            
                bd.restaurarStock(codigo,cant,t,co,linea);
            }
            datos.setRowCount(0);
            for(int i=0;i<datos.getRowCount();i++){
                datos.removeRow(i);
            }
            
//            limpiar();
            total();
            showMessageDialog(null, "Venta cancelada");
    }

    private void listarVentas(ArrayList<Venta> ve) {
        DefaultTableModel datos = (DefaultTableModel) listaVentas.jtVentas.getModel();
        datos.setNumRows(0);
        
        for(int i = 0;i<ve.size();i++){
            Object[] fila = {ve.get(i).getNroComprobante(),
                             ve.get(i).getFecha(),
                             ve.get(i).getTotal(),
                             ve.get(i).getCliente().getCuit()};            
            datos.addRow(fila);
        }
    }

    private void listarLineasDeVenta(ArrayList<Venta> ve) {
        
        DefaultTableModel datos = (DefaultTableModel) devoluciones.jtLinea.getModel();
        
        if(!ve.get(0).getLista().isEmpty()){
            datos.setNumRows(0);
        
            for(int i = 0;i<ve.get(0).getLista().size();i++){
                Object[] fila = {ve.get(0).getLista().get(i).getProducto().getCodigo(),
                    ve.get(0).getLista().get(i).getProducto().getDescripcion(),
                    ve.get(0).getLista().get(i).getCantidad(),
                    ve.get(0).getLista().get(i).getPrecioU(),
                    ve.get(0).getLista().get(i).getSubtotal(),
                    ve.get(0).getLista().get(i).getTalle().getDescripcion(),
                    ve.get(0).getLista().get(i).getColor().getDescripcion(),
                    ve.get(0).getLista().get(i).getIdLinea(),
                    ve.get(0).getNroComprobante()};            
                datos.addRow(fila);
            }
        }else{
            System.out.println("ENTRE");
            JOptionPane.showMessageDialog(null,"No existe Venta");
        }
    }
}
