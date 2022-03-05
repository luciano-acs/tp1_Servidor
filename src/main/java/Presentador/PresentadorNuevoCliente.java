/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentador;

import BD.ServidorBD;
import Modelo.ClienteT.Cliente;
import Modelo.ClienteT.CondTributaria;
import java.util.ArrayList;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorNuevoCliente{

    ServidorBD bd = new ServidorBD();

    public PresentadorNuevoCliente() {

    }

    public void nuevoCliente(String cuit, String cond, String domicilio, String email, String razon) {
        Cliente cl = new Cliente();
        cl.setCuit(cuit);
        cl.setCondicion(cond);
        cl.setDomicilio(domicilio);
        cl.setEmail(email);
        cl.setRazonSocial(razon);

        bd.registrarCliente(cl);
    }

    public ArrayList<String> cargarCombos() {
        ArrayList<String> condiciones = new ArrayList<>();
        for (CondTributaria cond : CondTributaria.values()) {
            condiciones.add(cond.getCondicion());
        }
        return condiciones;
    }
}
