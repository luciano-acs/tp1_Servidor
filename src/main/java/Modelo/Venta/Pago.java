package Modelo.Venta;

public class Pago {
    
    private int idPago;
    private double pago;
    private FormaDePago fdp;
    private Venta venta;

    public Pago(int idPago, double pago, FormaDePago fdp, Venta venta) {
        this.idPago = idPago;
        this.pago = pago;
        this.fdp = fdp;
        this.venta = venta;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }
    
    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public double getPago() {
        return pago;
    }

    public void setPago(double pago) {
        this.pago = pago;
    }

    public FormaDePago getFdp() {
        return fdp;
    }

    public void setFdp(FormaDePago fdp) {
        this.fdp = fdp;
    }
}
