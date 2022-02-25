package Presentador;

import Servidor.ServidorBD;
import Modelo.Organizacion.Empleado;
import Vista.pFactura;
import Vista.Sesion;
import Vista.nvoCliente;
import Vista.pClientes;
import Vista.pDevoluciones;
import Vista.pListarProductos;
import Vista.pListarVentas;
import Vista.pLogo;
import Vista.pModProductos;
import Vista.pProductos;
import Vista.pVentas;
import Vista.pgestiones;
import Vista.vistaMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import java.util.GregorianCalendar;
import javax.swing.table.DefaultTableModel;

public class PresentadorMenu implements ActionListener{
    
    vistaMenu principal = new vistaMenu();
    Sesion iniciar = new Sesion();
    pProductos productos = new pProductos();
    pVentas ventas = new pVentas();
    pClientes clientes = new pClientes();
    pListarProductos listaProd = new pListarProductos();
    pLogo logo = new pLogo();
    pModProductos modProductos = new pModProductos();
    pFactura factura = new pFactura();
    pgestiones gestion = new pgestiones();
    pDevoluciones devolucion = new pDevoluciones();
    pListarVentas listaVentas = new pListarVentas();
    
    ServidorBD bd = new ServidorBD();
    nvoCliente nvoCliente = new nvoCliente();
    
    public PresentadorMenu(vistaMenu vista, Sesion sesion, Empleado emp) {
        System.out.println("INGRESO CONSTRUCTOR MENU");        
        
        this.principal = vista;
        this.iniciar = sesion;
        this.principal.btnNvoProducto.addActionListener(this);
        this.principal.btnVentas.addActionListener(this);
        this.principal.btnClientes.addActionListener(this);
        this.principal.btnListarProd.addActionListener(this);
        this.principal.btnModProducto.addActionListener(this);
        this.principal.btnNvoCliente.addActionListener(this);
        this.principal.btnSalir.addActionListener(this);
        this.principal.btnGestiones.addActionListener(this);
        this.principal.btnListarVentas.addActionListener(this);
        this.principal.btnDevoluciones.addActionListener(this);
        
        principal.jlProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/moda.png")));
        principal.jlVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ventas (1).png"))); // NOI18N
        principal.jlClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cliente.png"))); // NOI18N
        principal.btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        principal.jlLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/percha (3).png"))); // NOI18N
        principal.jlLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/percha (3).png"))); // NOI18N
        
        principal.jlLogo.setVisible(false);
        principal.jlLogo1.setVisible(true);
                
        principal.add(productos); 
        principal.add(ventas); 
        principal.add(clientes); 
        principal.add(logo); 
        principal.add(listaProd); 
        principal.add(modProductos);
        principal.add(gestion);
        principal.add(devolucion);
        principal.add(listaVentas);
        
        logo.setVisible(true);
        productos.setVisible(false);
        ventas.setVisible(false);
        clientes.setVisible(false); 
        listaProd.setVisible(false);
        modProductos.setVisible(false);
        gestion.setVisible(false);
        devolucion.setVisible(false);
        listaVentas.setVisible(false);
        
        logo.setBounds(250, 100, 700, 700);
        productos.setBounds(250, 100, 700, 700);
        ventas.setBounds(250, 100, 700, 700);
        clientes.setBounds(250, 100, 700, 700);
        listaProd.setBounds(250, 100, 700, 700);
        modProductos.setBounds(250, 100, 700, 700);
        gestion.setBounds(250,100,700,700);
        devolucion.setBounds(250,100,700,700);
        listaVentas.setBounds(250,100,700,700);
        
        Calendar fecha = new GregorianCalendar();
        int mes = fecha.get(MONTH);
        principal.jlFecha.setText("Fecha: "+""+fecha.get(DAY_OF_MONTH)+"/"+(mes+1)+"/"+fecha.get(YEAR));
        principal.jlLegajo.setText(""+emp.getLegajo());
        principal.jlEmpleado.setText(emp.getApellido()+", "+emp.getNombre());
        
        if(emp.getRol().equals("G")){
            principal.btnListarProd.setEnabled(false);
            principal.btnModProducto.setEnabled(false);            
            principal.btnNvoProducto.setEnabled(false);
            principal.btnDevoluciones.setEnabled(false);
            principal.btnListarVentas.setEnabled(false); 
        }if(emp.getRol().equals("V")){
            principal.btnClientes.setEnabled(false);
            principal.btnNvoCliente.setEnabled(false);
            principal.btnGestiones.setEnabled(false);
            principal.btnListarProd.setEnabled(false);
            principal.btnModProducto.setEnabled(false);            
            principal.btnNvoProducto.setEnabled(false);
            principal.btnListarVentas.setEnabled(false);            
        }if(emp.getRol().endsWith("A")){
            principal.btnClientes.setEnabled(false);
            principal.btnNvoCliente.setEnabled(false);
            principal.btnListarVentas.setEnabled(false);
            principal.btnDevoluciones.setEnabled(false);
            principal.btnListarVentas.setEnabled(false); 
        }
    }

    PresentadorMenu(){
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==principal.btnNvoProducto){            
            principal.jlLogo.setVisible(true);
            principal.jlLogo1.setVisible(false);
            
            productos.setVisible(true); //
            logo.setVisible(false);
            ventas.setVisible(false);
            clientes.setVisible(false);
            listaProd.setVisible(false);            
            modProductos.setVisible(false);
            gestion.setVisible(false);
            
            PresentadorProductos pp = new PresentadorProductos(productos);
            pp.cargarCombos();
            pp.productos.jtfNombre.setEnabled(true);
        }
        if(e.getSource()==principal.btnVentas){
            principal.jlLogo.setVisible(true);
            principal.jlLogo1.setVisible(false);
            
            productos.setVisible(false);
            logo.setVisible(false);
            ventas.setVisible(true);     //
            clientes.setVisible(false);            
            listaProd.setVisible(false);
            modProductos.setVisible(false);
            gestion.setVisible(false);
            devolucion.setVisible(false);
            listaVentas.setVisible(false);
            
            PresentadorVentas pv = new PresentadorVentas(ventas,principal,0);      
            pv.cardarId();
            
        }
        if(e.getSource()==principal.btnClientes){
            principal.jlLogo.setVisible(true);
            principal.jlLogo1.setVisible(false);
            
            productos.setVisible(false);
            logo.setVisible(false);
            ventas.setVisible(false);     
            clientes.setVisible(true);          //  
            listaProd.setVisible(false);
            modProductos.setVisible(false);
            gestion.setVisible(false);
            devolucion.setVisible(false);
            listaVentas.setVisible(false);
            
            PresentadorClientes pc = new PresentadorClientes(clientes);
            pc.listarTabla();
        }
        if(e.getSource().equals(principal.btnNvoCliente)){
            logo.setVisible(false);
            ventas.setVisible(false);     
            clientes.setVisible(false);            
            listaProd.setVisible(false);
            modProductos.setVisible(false);
            gestion.setVisible(false);
            devolucion.setVisible(false);
            listaVentas.setVisible(false);
            
            PresentadorClientes pc = new PresentadorClientes(nvoCliente);
            pc.cargarCombo();
            nvoCliente.setVisible(true); //
            
        }
        if(e.getSource()==principal.btnListarProd){
            principal.jlLogo.setVisible(true);
            principal.jlLogo1.setVisible(false);
            
            productos.setVisible(false);
            listaProd.setVisible(true); //
            logo.setVisible(false);
            ventas.setVisible(false);     
            clientes.setVisible(false);
            modProductos.setVisible(false);
            gestion.setVisible(false);
            devolucion.setVisible(false); 
            listaVentas.setVisible(false);
            
            PresentadorProductos pp = new PresentadorProductos(listaProd);
            pp.listarTabla();
        } 
        if(e.getSource().equals(principal.btnModProducto)){
            principal.jlLogo.setVisible(true);
            principal.jlLogo1.setVisible(false);
            
            productos.setVisible(false);
            listaProd.setVisible(false);
            logo.setVisible(false);
            ventas.setVisible(false);     
            clientes.setVisible(false);
            modProductos.setVisible(true); //
            gestion.setVisible(false);            
            devolucion.setVisible(false);
            listaVentas.setVisible(false);
                        
            PresentadorProductos pp = new PresentadorProductos(modProductos,principal);
            pp.cargarCombosM();
        }
        if(e.getSource().equals(principal.btnGestiones)){
            principal.jlLogo.setVisible(true);
            principal.jlLogo1.setVisible(false);
            
            productos.setVisible(false);
            listaProd.setVisible(false);
            logo.setVisible(false);
            ventas.setVisible(false);     
            clientes.setVisible(false);
            modProductos.setVisible(false);
            gestion.setVisible(true); //
            devolucion.setVisible(false); 
            listaVentas.setVisible(false);
            
            PresentadorProductos pp = new PresentadorProductos(gestion);
            pp.cargarCombosGestiones();
            pp.listarTablaGestion();
        }
        if(e.getSource().equals(principal.btnDevoluciones)){
            principal.jlLogo.setVisible(true);
            principal.jlLogo1.setVisible(false);
            
            productos.setVisible(false);
            listaProd.setVisible(false);
            logo.setVisible(false);
            ventas.setVisible(false);     
            clientes.setVisible(false);
            modProductos.setVisible(false);
            gestion.setVisible(false); 
            devolucion.setVisible(true); //
            listaVentas.setVisible(false);
            
            PresentadorVentas pv = new PresentadorVentas(devolucion,ventas,principal);         
            pv.devoluciones.jtfSaldo.setText(""+0);
        }
        if(e.getSource().equals(principal.btnListarVentas)){
            principal.jlLogo.setVisible(true);
            principal.jlLogo1.setVisible(false);
            
            productos.setVisible(false);
            listaProd.setVisible(false);
            logo.setVisible(false);
            ventas.setVisible(false);     
            clientes.setVisible(false);
            modProductos.setVisible(false);
            gestion.setVisible(false); 
            devolucion.setVisible(false); 
            listaVentas.setVisible(true); //
            
            PresentadorVentas pv = new PresentadorVentas(listaVentas);            
        }
        if(e.getSource()==principal.btnSalir){
            principal.dispose(); 
            Sesion s = new Sesion();
            PresentadorInicio inicio = new PresentadorInicio(s);           
        }
        e.setSource("");
    }   
}
