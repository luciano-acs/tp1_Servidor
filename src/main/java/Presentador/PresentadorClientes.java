package Presentador;

import Servidor.ServidorBD;
import Modelo.ClienteT.Cliente;
import Modelo.ClienteT.CondTributaria;
import Modelo.Producto.Producto;
import Vista.nvoCliente;
import Vista.pClientes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.System.out;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luciano Acosta
 */
public class PresentadorClientes implements ActionListener{
    pClientes clientes = new pClientes();
    nvoCliente nvoCliente = new nvoCliente();
    ServidorBD bd = new ServidorBD();
    
    public PresentadorClientes(pClientes cl){
        this.clientes = cl;
        clientes.btnBuscarC.addActionListener(this);
        clientes.jtfBuscar.setText("");
    }

    PresentadorClientes(nvoCliente nvoCliente) {
        this.nvoCliente = nvoCliente;
        nvoCliente.btnRegistrarC.addActionListener(this);
        nvoCliente.jtfDomicilio.setText("");
        nvoCliente.jtfEmail.setText("");
        nvoCliente.jtfRazon.setText("");
    }

    void listarTabla() {
        DefaultTableModel datos = (DefaultTableModel) clientes.jtClientes.getModel();
        datos.setNumRows(0);
        
        ArrayList<Cliente> lista = bd.listarClientes();
        
        for(int i = 0;i<lista.size();i++){
            Object[] fila = {lista.get(i).getCuit(),
                             lista.get(i).getRazonSocial(),
                             lista.get(i).getDomicilio(),
                             lista.get(i).getEmail(),
                             lista.get(i).getCondicion()
                             };
            
            datos.addRow(fila);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(clientes.btnBuscarC)){
            buscarClientesTabla();
        }
        if(e.getSource().equals(nvoCliente.btnRegistrarC)){
            if(nvoCliente.jtlCUIT.getText().isEmpty()||nvoCliente.cbCond.getSelectedItem().toString().isEmpty()||
                    nvoCliente.jtfDomicilio.getText().isEmpty()||nvoCliente.jtfEmail.getText().isEmpty()||
                    nvoCliente.jtfRazon.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Campos vacios");
            }else{
                Cliente cl = new Cliente();
                cl.setCuit(nvoCliente.jtlCUIT.getText());
                cl.setCondicion(nvoCliente.cbCond.getSelectedItem().toString());
                cl.setDomicilio(nvoCliente.jtfDomicilio.getText());
                cl.setEmail(nvoCliente.jtfEmail.getText());
                cl.setRazonSocial(nvoCliente.jtfRazon.getText());
                
                bd.registrarCliente(cl);
                nvoCliente.dispose();
            }             
        }
        e.setSource("");
    }

    private void buscarClientesTabla() {
        String codigo = clientes.jtfBuscar.getText();
        
        for(int i = 0;i<clientes.jtClientes.getRowCount();i++){
            if(clientes.jtClientes.getValueAt(i, 0).toString().equals(codigo)){
                clientes.jLabel3.setText("");
                clientes.jtClientes.changeSelection(i, 0, false, false);
                break;
            }
            else{
                clientes.jLabel3.setText("Cliente no registrado");
            }
        }
    }

    void cargarCombo() {
        for(CondTributaria cond : CondTributaria.values()){
            nvoCliente.cbCond.addItem(cond.getCondicion());
        }
    }
    
    
}
