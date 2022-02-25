/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentador;

import Servidor.ServidorBD;
import Modelo.Organizacion.Empleado;
import Vista.Sesion;
import Vista.vistaMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.System.exit;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorInicio implements ActionListener{

    Sesion iniciar = new Sesion();
    ServidorBD bd = new ServidorBD();
    
    public PresentadorInicio(Sesion inicio) {
        this.iniciar = inicio;
        this.iniciar.btnIngresar.addActionListener(this);
        this.iniciar.btnSalir.addActionListener(this);    
        
        iniciar.jlLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/percha (3).png")));
        iniciar.btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png")));
        iniciar.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==iniciar.btnIngresar){
            Empleado emp = bd.existeEmpleado(iniciar.jtfLegajo.getText(),iniciar.jtfContraseña.getText());
            if(emp!=null){
                vistaMenu menu = new vistaMenu();
                PresentadorMenu pMenu = new PresentadorMenu(menu,iniciar,emp);
                menu.setVisible(true);
                iniciar.dispose();
            }else{
                JOptionPane.showMessageDialog(null, "Usuario y contraseña invalidos");
            }                        
        }
        if(e.getSource()==iniciar.btnSalir){
            exit(0);
        }
        e.setSource("");
    }
    
}
