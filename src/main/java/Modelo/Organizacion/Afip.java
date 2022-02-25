/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Organizacion;

/**
 *
 * @author Luciano Acosta
 */
public class Afip {
    private int idafip;
    private String CAE;
    private String VtoCAE;
    private String condicion;

    public Afip(int idafip, String CAE, String VtoCAE, String condicion) {
        this.idafip = idafip;
        this.CAE = CAE;
        this.VtoCAE = VtoCAE;
        this.condicion = condicion;
    }

    public Afip() {
        
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    
    public int getIdafip() {
        return idafip;
    }

    public void setIdafip(int idafip) {
        this.idafip = idafip;
    }

    public String getCAE() {
        return CAE;
    }

    public void setCAE(String CAE) {
        this.CAE = CAE;
    }

    public String getVtoCAE() {
        return VtoCAE;
    }

    public void setVtoCAE(String VtoCAE) {
        this.VtoCAE = VtoCAE;
    }   
    
}
