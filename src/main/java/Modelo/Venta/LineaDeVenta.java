package Modelo.Venta;
import Modelo.Producto.ColorP;
import Modelo.Producto.Producto;
import Modelo.Producto.Talle;
public class LineaDeVenta {

    private int idLinea;
    private Producto producto;
    private double precioU;
    private int cantidad;
    private double subtotal;
    private ColorP color;
    private Talle talle;

    public LineaDeVenta(int idLinea, Producto producto, double precioU ,int cantidad, double subtotal, ColorP color, Talle talle) {
        this.idLinea = idLinea;
        this.producto = producto;
        this.precioU = precioU;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.color = color;
        this.talle = talle;
    }

    public LineaDeVenta() {
        
    }

    public ColorP getColor() {
        return color;
    }

    public void setColor(ColorP color) {
        this.color = color;
    }

    public Talle getTalle() {
        return talle;
    }

    public void setTalle(Talle talle) {
        this.talle = talle;
    }
    
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getPrecioU() {
        return precioU;
    }

    public void setPrecioU(double precioU) {
        this.precioU = precioU;
    }

    public int getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(int idLinea) {
        this.idLinea = idLinea;
    }
}
