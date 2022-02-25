package Servidor;

import Modelo.ClienteT.Cliente;
import Modelo.Organizacion.Afip;
import Modelo.Organizacion.Empleado;
import Modelo.Producto.Auxiliar;
import Modelo.Producto.ColorP;
import Modelo.Producto.Marca;
import Modelo.Producto.Producto;
import Modelo.Producto.Rubro;
import Modelo.Producto.Stock;
import Modelo.Producto.Talle;
import Modelo.Producto.TipoDeTalle;
import Modelo.Venta.Factura;
import Modelo.Venta.FormaDePago;
import Modelo.Venta.LineaDeVenta;
import Modelo.Venta.Pago;
import Modelo.Venta.Venta;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Luciano Acosta
 */
public class ServidorBD {
    
    Connection getConnection(){
        Connection c = null;
        String ip;
        try{
            try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                ip = socket.getLocalAddress().getHostAddress();
//                System.out.println(ip);
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://192.168.100.125:3306/tp1","Tienda","");
//            System.out.println("CONECTADO");
        }
        catch(Exception e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null,"Conexion con servidor fallida \n"+e);
        }
        return c;
    }

    public ArrayList<Producto> listarProductos() {
        ArrayList<Producto> productos = new ArrayList<Producto>();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select a.codProducto, a.descripcion, a.costo, a.margenGanancia, a.porcIVA, a.visible, b.cantidad, c.idMarca, c.descripcion as Marca, d.idRubro, d.descripcion as Rubro from Producto as a, Stock as b, Marca as c, Rubro as d \n" +
                       "where a.Stock_idStock = b.idStock and\n" +
                       "a.Marca_idMarca = c.idMarca and\n" +
                       "a.Rubro_idRubro = d.idRubro and\n" +
                       "a.visible = 1";
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              Marca marca = new Marca(r.getInt("idMarca"),r.getString("Marca")); 
              Rubro rubro = new Rubro(r.getInt("idRubro"),r.getString("Rubro"));
              Producto p = new Producto();
              p.setCodigo(r.getInt("codProducto"));
              p.setDescripcion(r.getString("descripcion"));
              p.setPorcIVA(r.getDouble("porcIva"));
              p.setCosto(r.getDouble("costo"));
              p.setMargenGanancia(r.getDouble("margenGanancia"));              
              p.setMarca(marca);
              p.setRubro(rubro);
              p.setVisible(1);
              productos.add(p);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return productos;
    }
    
    public Producto buscarProducto(String codigo){
        Producto p = new Producto();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select a.codProducto, a.descripcion, a.costo, a.margenGanancia, a.porcIVA, b.cantidad, c.idMarca, c.descripcion as Marca, d.idRubro, d.descripcion as Rubro from Producto as a, Stock as b, Marca as c, Rubro as d \n" +
                       "where a.Stock_idStock = b.idStock and\n" +
                       "a.Marca_idMarca = c.idMarca and\n" +
                       "a.Rubro_idRubro = d.idRubro and a.codProducto = %1".replace("%1", codigo);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              Stock st = buscarStock(r.getString("a.codProducto"));
              Marca marca = new Marca(r.getInt("idMarca"),r.getString("Marca")); 
              Rubro rubro = new Rubro(r.getInt("idRubro"),r.getString("Rubro"));              
              p.setCodigo(r.getInt("codProducto"));
              p.setDescripcion(r.getString("descripcion"));
              p.setPorcIVA(r.getDouble("porcIva"));
              p.setCosto(r.getDouble("costo"));
              p.setMargenGanancia(r.getDouble("margenGanancia"));              
              p.setMarca(marca);
              p.setRubro(rubro);
              p.setStock(st);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return p;
    }

    public ArrayList<Marca> listarMarcas() {
        ArrayList<Marca> marcas = new ArrayList<Marca>();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select * from Marca";
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              Marca m = new Marca(r.getInt("idMarca"),r.getString("descripcion")); 
              
              marcas.add(m);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return marcas;
    }

    public ArrayList<TipoDeTalle> listarTipoTalle() {
        ArrayList<TipoDeTalle> tipos = new ArrayList<TipoDeTalle>();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select * from tipodetalle";
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              TipoDeTalle t = new TipoDeTalle(r.getInt("idTipoTalle"),r.getString("descripcion")); 
              
              tipos.add(t);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return tipos;
    }

    public int buscarCodTipo(String buscar) {
        int cod = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select idTipoTalle from tipodetalle where descripcion='%1'".replace("%1", buscar);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              cod = r.getInt("idTipoTalle");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
       return cod;
    }
    
    public int buscarCodMarca(String marca) {
        int cod = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select idMarca from marca where descripcion='%1'".replace("%1", marca);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              cod = r.getInt("idMarca");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
       return cod;
    }

    public int buscarCodRubro(String rubro) {
        int cod = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select idRubro from rubro where descripcion='%1'".replace("%1", rubro);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              cod = r.getInt("idRubro");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
       return cod;
    }
    
    public int buscarCodTalle(String talle) {
        int cod = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select idTalle from talle where descripcion='%1'".replace("%1", talle);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              cod = r.getInt("idTalle");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
       return cod;
    }

    public int buscarCodColor(String color) {
        int cod = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select idColor from color where descripcion='%1'".replace("%1", color);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              cod = r.getInt("idColor");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
       return cod;
    }

    public ArrayList<Talle> buscarTalle(int buscarCodTipo) {
        ArrayList<Talle> talles = new ArrayList<Talle>();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select distinct a.idTalle, a.descripcion from talle as a INNER jOIN tipodetalle as b where a.Tipodetalle_idTipoTalle = %1;".replace("%1",""+buscarCodTipo);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              Talle ta = new Talle(r.getInt("idTalle"),r.getString("descripcion")); 
              
              talles.add(ta);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return talles;
    }

    public ArrayList<ColorP> listarColor() {
        ArrayList<ColorP> colores = new ArrayList<ColorP>();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select * from color";
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              ColorP co = new ColorP(r.getInt("idColor"),r.getString("descripcion")); 
              
              colores.add(co);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return colores;
    }

    public ArrayList<Rubro> listarRubros() {
        ArrayList<Rubro> rubros = new ArrayList<Rubro>();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select * from rubro";
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              Rubro ru = new Rubro(r.getInt("idRubro"),r.getString("descripcion")); 
              
              rubros.add(ru);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return rubros;
    } 

    public void agregarProducto(Producto p, Stock s, Marca m, Rubro r) {
        try{
            String cadena1 = "insert into stock values (%1,%2)"
            .replace("%1",""+s.getId())
            .replace("%2",""+s.getCantidad());            
            
            String cadena2 = "insert into producto values (%1,'%2',%3,%4,%5,%6,%7,%8,%9)"
           .replace("%1",""+p.getCodigo()) 
           .replace("%2",p.getDescripcion())
           .replace("%3",""+p.getCosto())  
           .replace("%4",""+p.getMargenGanancia())
           .replace("%5",""+p.getPorcIVA())
           .replace("%6",""+s.getId())
           .replace("%7",""+m.getCodMarca())
           .replace("%8",""+r.getCodRubro())
           .replace("%9",""+1);
            
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1);    
            c.createStatement().executeUpdate(cadena2);
        }
        catch(Exception e){
            e.printStackTrace();
        } 
    }

    public int ultimoCod() {
        int cod = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select max(idVenta) from venta";
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              cod = r.getInt("max(idVenta)");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return cod+1;
    }
    
    public int ultimoCodMarca() {
        int cod = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select max(idMarca) from marca";
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              cod = r.getInt("max(idMarca)");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return cod+1;
    }
    
    public int ultimoCodColor() {
        int cod = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select max(idColor) from color";
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              cod = r.getInt("max(idColor)");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return cod+1;
    }
    
    public Producto listarProducto(int codigo) {
        Producto p = new Producto();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select a.codProducto, a.descripcion, a.costo, a.margenGanancia, a.porcIVA, a.Stock_idStock, a.Marca_idMarca, a.Rubro_idRubro, a.visible from producto as a where a.codProducto=%1"
                        .replace("%1",""+codigo);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              Marca m = new Marca(r.getInt("Marca_idMarca"),buscarDMarca(r.getInt("Marca_idMarca")));
              Rubro ru = new Rubro(r.getInt("Rubro_idRubro"),buscarDRubro(r.getInt("Rubro_idRubro")));
              Stock stock = new Stock(r.getInt("Stock_idStock"),buscarCStock(r.getInt("Stock_idStock")));
              p.setCodigo(r.getInt("codProducto"));
              p.setDescripcion(r.getString("descripcion"));
              p.setCosto(r.getDouble("costo"));
              p.setMargenGanancia(r.getDouble("margenGanancia"));
              p.setPorcIVA(r.getDouble("porcIVA"));
              p.setStock(stock);
              p.setMarca(m);
              p.setRubro(ru);
              p.setVisible(r.getInt("visible"));
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return p;
    }    

    public ArrayList<FormaDePago> listarFormas() {
        ArrayList<FormaDePago> formas = new ArrayList<FormaDePago>();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select idForma, descripcion from formadepago";
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              FormaDePago forma = new FormaDePago();
              forma.setIdForma(r.getInt("idForma"));
              forma.setDescripcion(r.getString("descripcion"));
              
              formas.add(forma);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return formas;
    }
    
    public String buscarDMarca(int marca) {
        String descripcion = "";
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select descripcion from marca where idMarca=%1".replace("%1",""+marca);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              descripcion = r.getString("descripcion");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
       return descripcion;
    }

    public String buscarDRubro(int rubro) {
        String descripcion = "";
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select descripcion from rubro where idRubro=%1".replace("%1",""+rubro);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              descripcion = r.getString("descripcion");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
       return descripcion;
    }
    
    public String buscarDColor(int color) {
        String descripcion = "";
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select descripcion from color where idcolor=%1".replace("%1",""+color);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              descripcion = r.getString("descripcion");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
       return descripcion;
    }
    
    public String buscarDTalle(int talle) {
        String descripcion = "";
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select descripcion from talle where idTalle=%1".replace("%1",""+talle);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              descripcion = r.getString("descripcion");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
       return descripcion;
    }
    
    public void modificarProducto(Producto p, Marca m, Rubro ru) {
        try{
            String cadena1 = "update producto set descripcion='%2', costo=%3, margenGanancia=%4, porcIVA=%5, Marca_idMarca=%6, Rubro_idRubro=%7 where codProducto = %1"
                            .replace("%1",""+p.getCodigo())
                            .replace("%2",p.getDescripcion())
                            .replace("%3",""+p.getCosto())
                            .replace("%4",""+p.getMargenGanancia())
                            .replace("%5",""+p.getPorcIVA())
                            .replace("%6",""+m.getCodMarca())
                            .replace("%7",""+ru.getCodRubro());
          
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1); 
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public Cliente buscarCliente(String cuit) {
        Cliente cl = new Cliente();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select a.cuit, a.razonSocial, a.domicilio, a.email, a.cond_tributaria from cliente as a where a.cuit='%1'"
                        .replace("%1",""+cuit);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              cl.setCuit(r.getString("a.cuit"));
              cl.setRazonSocial(r.getString("a.razonSocial"));
              cl.setDomicilio(r.getString("a.domicilio"));
              cl.setEmail(r.getString("a.email"));
              cl.setCondicion(r.getString("a.cond_tributaria"));
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return cl;
    }

    private int buscarCStock(int idStock) {
        int cantidad = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select cantidad from stock where idStock='%1'".replace("%1",""+idStock);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              cantidad = r.getInt("cantidad");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
       return cantidad;
    }
    
    public void agregarStockN(Stock s) {
        try{
            String cadena1 = "insert into stock values (%1,%2)"
                                .replace("%1",""+s.getId()) 
                                .replace("%2",""+s.getCantidad());          
            
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1); 
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void agregarStock(int cod, Talle t, ColorP co, int cantidadS) {
        try{
            String cadena1 = "insert into color_has_talle values (%1,%2,%3,%4)"
                                .replace("%1",""+co.getIdColor()) 
                                .replace("%2",""+t.getIdTalle())
                                .replace("%3",""+cantidadS)
                                .replace("%4",""+cod);          
            
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1); 
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void eliminarProducto(int codProducto) {
        try{
            String cadena1 = "update producto set visible=0 where codProducto = %1"
                            .replace("%1",""+codProducto);
          
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1); 
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<ColorP> listarStockColor(int cod) {
        ArrayList<ColorP> colores = new ArrayList<ColorP>();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select DISTINCT a.idColor, a.descripcion, d.cantidad from color as a, producto as b, stock as c, color_has_talle as d \n" +
                        "where b.Stock_idStock = c.idStock and\n" +
                        "c.idStock = d.Stock_idStock and\n" +
                        "d.Color_idColor = a.idColor and\n" +
                        "b.codProducto = %1 and d.cantidad > 0".replace("%1",""+cod);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              ColorP co = new ColorP(r.getInt("idColor"),r.getString("descripcion"));
              colores.add(co);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return colores;
    }

    public ArrayList<TipoDeTalle> listarStockTipoTalle(int cod) {
        ArrayList<TipoDeTalle> tipos = new ArrayList<TipoDeTalle>();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select DISTINCT e.idTipoTalle, e.descripcion from talle as a, producto as b, stock as c, color_has_talle as d, tipodetalle as e \n" +
                        "where b.Stock_idStock = c.idStock and\n" +
                        "c.idStock = d.Stock_idStock and\n" +
                        "d.Talle_idTalle = a.idTalle and\n" +
                        "a.Tipodetalle_idTipoTalle = e.idTipoTalle and\n" +
                        "b.codProducto = %1".replace("%1",""+cod);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              TipoDeTalle t = new TipoDeTalle(r.getInt("idTipoTalle"),r.getString("descripcion"));
              tipos.add(t);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return tipos;
    }

    public ArrayList<Talle> buscarStockTalle(int cod, String tipo) {
        ArrayList<Talle> talles = new ArrayList<Talle>();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select distinct a.idTalle, a.descripcion, d.cantidad from talle as a, producto as b, stock as c, color_has_talle as d, tipodetalle as e\n" +
                        "where b.codProducto = c.idStock\n" +
                        "and c.idStock = d.Stock_idStock\n" +
                        "and d.Talle_idTalle = a.idTalle\n" +
                        "and a.Tipodetalle_idTipoTalle = e.idTipoTalle\n" +
                        "and e.idTipoTalle = %1 and b.codProducto = %2 and d.cantidad > 0"
                        .replace("%1",""+cod)
                        .replace("%2",tipo);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              Talle t = new Talle(r.getInt("a.idTalle"),r.getString("a.descripcion"));
              talles.add(t);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return talles;
    }

    public void registrarVenta(Venta v, int pto) {
        try{
            String cadena1 = "insert into venta values (%1,%2,%3,%4,%5)"
            .replace("%1",""+v.getNroComprobante())
            .replace("%2",v.getFecha())
            .replace("%3",""+v.getTotal())
            .replace("%4",""+pto)
            .replace("%5",""+v.getCliente().getCuit());
           
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1);    
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void actualizarStock(int codigo, int cantidad, int id, Talle t, ColorP co, double subtotal, double precio) {
        try{
            String cadena1 = "update stock as a INNER JOIN producto as b set a.cantidad = a.cantidad - %1 where a.idStock = b.Stock_idStock and b.codProducto = %2"
            .replace("%1",""+cantidad)
            .replace("%2",""+codigo);             
                
            String cadena2 = "update color_has_talle as a INNER JOIN talle as b, producto as c, stock as d, color as e set a.cantidad = a.cantidad - %1 where a.Stock_idStock = d.idStock and d.idStock = c.Stock_idStock and a.Talle_idTalle = b.idTalle and a.Color_idColor = e.idColor and c.codProducto = %2 and b.idTalle = %3 and e.idColor=%4"
                              .replace("%1",""+cantidad)
                              .replace("%2",""+codigo)
                              .replace("%3",""+t.getIdTalle())
                              .replace("%4",""+co.getIdColor());
                
            String cadena3 = "insert into lineadeventa values (null,%2,%3,%4,%5,%6,%7,%8)"
            .replace("%2",""+cantidad)
            .replace("%3",""+id)
            .replace("%4",""+codigo)
            .replace("%5",""+subtotal)
            .replace("%6",""+precio)
            .replace("%7",""+t.getIdTalle())
            .replace("%8",""+co.getIdColor());
                
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1);   
            c.createStatement().executeUpdate(cadena2); 
            c.createStatement().executeUpdate(cadena3);                       
        }
        catch(Exception e){
            e.printStackTrace();
        } 
    }

    public void actualizarStockG(int codigo, int cantidad, Talle t, ColorP co) {
        try{
            String cadena1 = null;
            String cadena2 = null;
            Connection c = getConnection();
            
            cadena1 = "update stock as a INNER JOIN producto as b set a.cantidad = a.cantidad + %1 where a.idStock = b.Stock_idStock and b.codProducto = %2"
                        .replace("%1",""+cantidad)
                        .replace("%2",""+codigo);
            c.createStatement().executeUpdate(cadena1); 
            if(existeColorTalle(codigo, co.getIdColor(),t.getIdTalle())){
                cadena2 = "update color_has_talle inner join producto, color, talle, stock set color_has_talle.cantidad = color_has_talle.cantidad + %1 where producto.codProducto = stock.idStock and stock.idStock = color_has_talle.Stock_idStock and color_has_talle.Color_idColor = %4 and color_has_talle.Talle_idTalle = %3 and producto.codProducto=%2"
                           .replace("%1",""+cantidad)
                           .replace("%2",""+codigo)
                           .replace("%3",""+t.getIdTalle())
                           .replace("%4",""+co.getIdColor());
            }else{
                cadena2 = "insert into color_has_talle values(%1,%2,%3,%4)"
                           .replace("%1",""+co.getIdColor())
                           .replace("%2",""+t.getIdTalle())
                           .replace("%3",""+cantidad)
                           .replace("%4",""+codigo);                        
            }                                    
              
            c.createStatement().executeUpdate(cadena2);                       
        }
        catch(Exception e){
            e.printStackTrace();
        } 
    }
    
    public int consultarStock(String codigo) {
        int cantidad = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select a.idStock, a.cantidad from stock as a where a.idStock=%1"
                        .replace("%1",""+codigo);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              cantidad = r.getInt("cantidad");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return cantidad;
    }
    
    public boolean existe(String codigo) {
        boolean existe = false;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select count(codProducto) From producto where codProducto=%1 and visible=1"
                        .replace("%1",""+codigo);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              if(r.getInt("count(codProducto)")==1){
                  existe = true;
              }
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return existe;
    }
    
    public boolean existeColorTalle(int codigo, int color, int talle) {
        boolean existe = false;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select count(a.codProducto) from Producto as a, Color as b, Color_has_talle as c, stock as d, talle as e\n" +
                        "where a.codProducto = d.idStock\n" +
                        "and d.idStock = c.Stock_idStock\n" +
                        "and b.idColor = c.Color_idColor\n" +
                        "and e.idTalle = c.Talle_idTalle\n" +
                        "and a.codProducto = %1 and b.idColor = %2 and e.idTalle = %3"
                        .replace("%1",""+codigo)
                        .replace("%2",""+color)
                        .replace("%3",""+talle);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              if(r.getInt("count(a.codProducto)")==1){
                  existe = true;
              }
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return existe;
    }
    
    
    public Empleado existeEmpleado(String user, String contraseña) {
        Empleado emp = new Empleado();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select count(legajo), apellido, nombre, email, rol from empleado where legajo=%1 and contraseña='%2'"
                        .replace("%1",""+user)
                        .replace("%2",contraseña);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              if(r.getInt("count(legajo)")==1){
                  emp.setApellido(r.getString("apellido"));
                  emp.setNombre(r.getString("nombre"));
                  emp.setLegajo(Integer.parseInt(user));
                  emp.setContraseña(contraseña);
                  emp.setEmail(r.getString("email"));
                  emp.setRol(r.getString("rol"));
              }else{
                  emp = null;
              }                  
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return emp;
    }
    
    public int cantTalle(String codigo, int talle){
        int cantidad = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select a.idTalle, a.descripcion, d.cantidad from talle as a, producto as b, stock as c, color_has_talle as d \n" +
                        "where b.Stock_idStock = c.idStock and\n" +
                        "c.idStock = d.Stock_idStock and\n" +
                        "d.Talle_idTalle = a.idTalle and b.codProducto = %1 and a.idTalle = %2"
                        .replace("%1",""+codigo)
                        .replace("%2",""+talle);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              cantidad = r.getInt("cantidad");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return cantidad;
    }
    
    public int cantColor(String cod, int co){
        int cantidad = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select a.idColor, a.descripcion, d.cantidad from color as a, producto as b, stock as c, color_has_talle as d \n" +
                        "where b.Stock_idStock = c.idStock and\n" +
                        "c.idStock = d.Stock_idStock and\n" +
                        "d.Color_idColor = a.idColor and\n" +
                        "b.codProducto = %1 and a.idColor=%2"
                        .replace("%1",""+cod)
                        .replace("%2",""+co);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              cantidad = r.getInt("cantidad");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return cantidad;
    }

    public void restaurarStock(String codigo, int cant, Talle t, ColorP co, int idVenta) {
        try{
            String cadena1 = "update stock as a INNER JOIN producto as b set a.cantidad = a.cantidad + %1 where a.idStock = b.Stock_idStock and b.codProducto = %2"
            .replace("%1",""+cant)
            .replace("%2",""+codigo);             
                
            String cadena2 = "update color_has_talle as a INNER JOIN talle as b, producto as c, stock as d, color as e set a.cantidad = a.cantidad + %1 where a.Stock_idStock = d.idStock and d.idStock = c.Stock_idStock and a.Talle_idTalle = b.idTalle and a.Color_idColor = e.idColor and c.codProducto = %2 and b.idTalle = %3 and e.idColor=%4"
                              .replace("%1",""+cant)
                              .replace("%2",""+codigo)
                              .replace("%3",""+t.getIdTalle())
                              .replace("%4",""+co.getIdColor());
            
            String cadena3 = "delete from lineadeventa where Venta_idVenta = %1".replace("%1",""+idVenta);
            
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1);   
            c.createStatement().executeUpdate(cadena2); 
            c.createStatement().executeUpdate(cadena3);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void restaurarStockL(String codigo, int cant, Talle t, ColorP co) {
        try{
            String cadena1 = "update stock as a INNER JOIN producto as b set a.cantidad = a.cantidad + %1 where a.idStock = b.Stock_idStock and b.codProducto = %2"
            .replace("%1",""+cant)
            .replace("%2",""+codigo);             
                
            String cadena2 = "update color_has_talle as a INNER JOIN talle as b, producto as c, stock as d, color as e set a.cantidad = a.cantidad + %1 where a.Stock_idStock = d.idStock and d.idStock = c.Stock_idStock and a.Talle_idTalle = b.idTalle and a.Color_idColor = e.idColor and c.codProducto = %2 and b.idTalle = %3 and e.idColor=%4"
                              .replace("%1",""+cant)
                              .replace("%2",""+codigo)
                              .replace("%3",""+t.getIdTalle())
                              .replace("%4",""+co.getIdColor());
            
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1);   
            c.createStatement().executeUpdate(cadena2);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void registrarFactura(Factura fact) {
        try{
            String cadena1 = "insert into factura values (%1,'%2',%3,%4,%5,%6)"
            .replace("%1",""+fact.getNumFact())
            .replace("%2",fact.getFecha())
            .replace("%3",""+fact.getTotal())
            .replace("%4",""+fact.getVenta().getNroComprobante())
            .replace("%5",""+fact.getNumFact())
            .replace("%6",""+1);
           
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1);    
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    
    public FormaDePago buscarFormaPago(String descripcion) {
        FormaDePago fdp = new FormaDePago();
        int cod = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select idForma, descripcion from formadepago where descripcion='%1'".replace("%1", descripcion);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              fdp.setIdForma(r.getInt("idForma"));
              fdp.setDescripcion(descripcion);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return fdp;
    }

    public void registrarPago(Pago pago) {
        try{
            String cadena1 = "insert into pago values (%1,%2,%3,%4)"
            .replace("%1",""+pago.getIdPago())
            .replace("%2",""+pago.getPago())
            .replace("%3",""+pago.getFdp().getIdForma())
            .replace("%4",""+pago.getVenta().getNroComprobante());
           
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1);    
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public Venta buscarVenta(int id) {
        Venta venta = new Venta();
        int cod = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select idVenta, fecha, total, Punto_de_Venta_idPDV, Cliente_cuit from venta where idVenta=%1".replace("%1",""+id);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              Cliente cl = buscarCliente(r.getString("Cliente_cuit"));
              venta.setCliente(cl);
              venta.setFecha(r.getString("fecha"));
              venta.setNroComprobante(Integer.parseInt(""+id));
              venta.setTotal(r.getDouble("total"));
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return venta;
    }

    public ArrayList<Cliente> listarClientes() {
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select * from cliente";
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              Cliente cl = new Cliente();
              cl.setCuit(r.getString("cuit"));
              cl.setCondicion(r.getString("cond_Tributaria"));
              cl.setDomicilio(r.getString("domicilio"));
              cl.setEmail(r.getString("email"));
              cl.setRazonSocial(r.getString("razonSocial"));
              
              clientes.add(cl);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return clientes;
    }

    public void registrarCliente(Cliente cl) {
        try{
            String cadena1 = "insert into cliente values ('%1','%2','%3','%4','%5')"
            .replace("%1",cl.getCuit())
            .replace("%2",cl.getRazonSocial())
            .replace("%3",cl.getDomicilio())
            .replace("%4",cl.getEmail())
            .replace("%5",cl.getCondicion());
           
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1);    
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void agregarColor(ColorP color) {
        try{
            String cadena1 = "insert into color values (%1,'%2')"
            .replace("%1",""+color.getIdColor())
            .replace("%2",color.getDescripcion());
           
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1);    
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void agregarMarca(Marca marca) {
        try{
            String cadena1 = "insert into marca values (%1,'%2')"
            .replace("%1",""+marca.getCodMarca())
            .replace("%2",marca.getDescripcionM());
           
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1);    
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Auxiliar> listarCaracteristicas(String codigo) {
        ArrayList<Auxiliar> caracteristicas = new ArrayList<Auxiliar>();
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select a.codProducto, b.descripcion, c.descripcion, d.cantidad from producto as a, color as b, talle as c, color_has_talle as d, stock as f\n" +
                        "where  a.codProducto = f.idStock\n" +
                        "and f.idStock = d.Stock_idStock\n" +
                        "and b.idColor = d.Color_idColor \n" +
                        "and c.idTalle = d.Talle_idTalle\n" +
                        "and a.codProducto = '%1'".replace("%1",""+codigo);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              Auxiliar aux = new Auxiliar();
              aux.setCantidad(r.getString("d.cantidad"));
              aux.setColor(r.getString("b.descripcion"));
              aux.setTalle(r.getString("c.descripcion"));
              Object[] agregar = {};              
              caracteristicas.add(aux);
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return caracteristicas;
    }

    public int consultarStockParticular(String codigo, int color, int talle) {
        int existencia = 0;
        try {
          Connection c = getConnection();
          Statement s = c.createStatement();
          String sql = "select a.codProducto, b.descripcion, c.descripcion, d.cantidad from producto as a, color as b, talle as c, color_has_talle as d, stock as f\n" +
                        "where  a.codProducto = f.idStock\n" +
                        "and f.idStock = d.Stock_idStock\n" +
                        "and b.idColor = d.Color_idColor\n" +
                        "and c.idTalle = d.Talle_idTalle\n" +
                        "and a.codProducto = '%1' and b.idColor = %2 and c.idTalle = %3"
                        .replace("%1",""+codigo)
                        .replace("%2",""+color)
                        .replace("%3",""+talle);
          ResultSet r = s.executeQuery(sql);
                  
          while(r.next()){
              existencia = r.getInt("d.cantidad");
          }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return existencia;
    }

    public ArrayList<Venta> busquedaVenta(long cuit, String tipo, int codigo) {
        ArrayList<Venta> ventas = new ArrayList<Venta>();
        try {
            Connection c = getConnection();
            Statement s = c.createStatement();
            String sql = "select a.idVenta, a.fecha, a.total, b.numFactura, b.visible, c.cuit, c.razonSocial, c.domicilio, c.email, c.cond_tributaria from venta as a, factura as b, cliente as c\n" +
                        "where a.idVenta = b.Venta_idVenta\n" +
                        "and a.Cliente_cuit = c.cuit\n" +
                        "and c.cuit = %1 and %2 = %3"
                        .replace("%1",""+cuit)
                        .replace("%2",tipo)
                        .replace("%3",""+codigo);
            ResultSet r = s.executeQuery(sql);
                  
            while(r.next()){
                Cliente cl = new Cliente();
                cl.setCuit(r.getString("c.cuit"));
                cl.setCondicion(r.getString("c.cond_tributaria"));
                cl.setDomicilio(r.getString("c.domicilio"));
                cl.setEmail(r.getString("c.email"));
                cl.setRazonSocial(r.getString("c.razonSocial"));
              
                Venta v = new Venta();
                v.setCliente(cl);
                v.setFecha(r.getString("a.fecha"));
                v.setNroComprobante(r.getInt("a.idventa"));
                v.setTotal(r.getDouble("a.total"));
              
                ArrayList<LineaDeVenta> li = busquedaLineaDeVenta(r.getInt("a.idVenta")); 
                v.setLista(li);

                ventas.add(v);
            }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return ventas;
    }

    public ArrayList<LineaDeVenta> busquedaLineaDeVenta(int idVenta) {
        ArrayList<LineaDeVenta> lineas = new ArrayList<LineaDeVenta>();
        try {
            Connection c = getConnection();
            Statement s = c.createStatement();
            String sql = "select a.idLinea, a.Color_idColor, a.Talle_idTalle, b.descripcion, b.codProducto, a.cantidad, a.subtotal, a.precioU from lineadeventa as a, producto as b, venta as c\n" +
                          "where c.idVenta = a.Venta_idVenta\n" +
                          "and a.Producto_codProducto = b.codProducto\n" +
                          "and c.idVenta = %1"
                        .replace("%1",""+idVenta);
            ResultSet r = s.executeQuery(sql);
                  
            while(r.next()){
                Producto p = buscarProducto(r.getString("b.codProducto"));   
                ColorP co = new ColorP(r.getInt("Color_idColor"),buscarDColor(r.getInt("a.Color_idColor")));
                Talle talle = new Talle(r.getInt("Talle_idTalle"),buscarDTalle(r.getInt("a.Talle_idTalle")));
                LineaDeVenta li = new LineaDeVenta();
                li.setIdLinea(r.getInt("a.idLinea"));
                li.setCantidad(r.getInt("a.cantidad"));
                li.setProducto(p);
                li.setSubtotal(r.getDouble("a.subtotal"));
                li.setPrecioU(r.getDouble("a.precioU"));
                li.setColor(co);
                li.setTalle(talle);
                lineas.add(li);
            }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return lineas;
    }

    private Stock buscarStock(String idStock) {
        Stock st = new Stock();
        try {
            Connection c = getConnection();
            Statement s = c.createStatement();
            String sql = "select a.idStock, a.cantidad from stock as a where idStock = %1"
                        .replace("%1",""+idStock);
            ResultSet r = s.executeQuery(sql);
                  
            while(r.next()){
                st.setId(r.getInt("a.idStock"));
                st.setCantidad(r.getInt("a.cantidad"));
            }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return st;
    }

    public Afip buscarFacturaAfip(int numero) {
        Afip afip = new Afip();
        try {
            Connection c = getConnection();
            Statement s = c.createStatement();
            String sql = "select a.idAfip, a.CAE, a.fechaVtoCAE, a.condicion from afip as a INNER JOIN factura as b\n" +
                         "where a.idAfip = b.Afip_idAfip\n" +
                         "and b.numFactura = %1"
                        .replace("%1",""+numero);
            ResultSet r = s.executeQuery(sql);
                  
            while(r.next()){
                afip.setCAE(r.getString("a.CAE"));
                afip.setIdafip(r.getInt("a.idAfip"));
                afip.setVtoCAE(r.getString("a.fechaVtoCAE"));
                afip.setCondicion(r.getString("a.condicion"));
            }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return afip;
    }

    public Factura buscarFactura(int idVenta) {
        Factura fact = new Factura();
        try {
            Connection c = getConnection();
            Statement s = c.createStatement();
            String sql = "select a.numFactura, a.fecha, a.total, a.Venta_idVenta, a.Afip_idAfip from factura as a INNER JOIN venta as b \n" +
                         "where a.Venta_idVenta = b.idVenta\n" +
                         "and  a.Venta_idVenta = %1"
                        .replace("%1",""+idVenta);
            ResultSet r = s.executeQuery(sql);
                  
            while(r.next()){
                fact.setNumFact(r.getInt("a.numFactura"));
                fact.setFecha(r.getString("a.fecha"));
                fact.setTotal(r.getDouble("a.total"));
                fact.setVenta(buscarVenta(idVenta));
            }          
        } catch(Exception e){
            e.printStackTrace();
        }
        return fact;
    }

    public void registarFacturaAfip(int numFact, String cae, String caeFchVto, String condicion) {
        try{
            String cadena1 = "insert into afip values (%1,'%2','%3','%4')"
                                .replace("%1",""+numFact) 
                                .replace("%2",cae)
                                .replace("%3",caeFchVto)
                                .replace("%4",condicion);          
            
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1); 
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void actualizarLinea(int linea, int cantidad) {
        try{
            String cadena1 = "update lineadeventa set cantidad = cantidad - %1 where idLinea = %2"
                                .replace("%1",""+cantidad) 
                                .replace("%2",""+linea);          
            
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1); 
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void actualizarVenta(int idVenta) {
        try{
            String cadena1 = "update venta set total = 0 where idVenta = %1"
                                .replace("%1",""+idVenta);          
            
            Connection c = getConnection();
            c.createStatement().executeUpdate(cadena1); 
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
}
