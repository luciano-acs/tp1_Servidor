/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentador;

import BD.ServidorBD;
import Modelo.Organizacion.Empleado;
import java.util.ArrayList;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorInicio{

    ServidorBD bd = new ServidorBD();
    public PresentadorInicio() {
        
    }

    public ArrayList<String> ingresarSistema(String legajo, String contraseña) {
        ArrayList<String> empleado = new ArrayList<String>();
        
        Empleado emp = bd.existeEmpleado(legajo,contraseña);
        
        empleado.add(""+emp.getLegajo());
        empleado.add(emp.getNombre());
        empleado.add(emp.getApellido());
        empleado.add(emp.getEmail());
        empleado.add(emp.getContraseña());
        empleado.add(emp.getRol());        
        
        return empleado;
    }
}
