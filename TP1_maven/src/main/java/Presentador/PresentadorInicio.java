/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentador;

import Vista.Sesion;
import Vista.vistaMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.System.exit;
import javax.swing.ImageIcon;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorInicio implements ActionListener{

    Sesion iniciar = new Sesion();
    
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
            vistaMenu menu = new vistaMenu();
            PresentadorMenu pMenu = new PresentadorMenu(menu,iniciar);
            menu.setVisible(true);
            iniciar.dispose();            
        }
        if(e.getSource()==iniciar.btnSalir){
            exit(0);
        }
    }
    
}
