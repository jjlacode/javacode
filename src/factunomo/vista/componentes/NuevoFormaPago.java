package factunomo.vista.componentes;

import java.awt.BorderLayout;



import java.lang.Object;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;

import factunomo.modelo.ComboModel;
import factunomo.modelo.Modelo;
import factunomo.modelo.obj.ConfiguracionVO;
import factunomo.modelo.obj.FormaPagoVO;
import factunomo.modelo.obj.GastoVO;
import factunomo.modelo.obj.DetalleGastoVO;
import factunomo.modelo.obj.ProductoVO;
import factunomo.utils.DateUtils;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


import com.sun.pdfview.*;





public class NuevoFormaPago extends FormularioBase{
	 private JDialog ventana;
	 private Container contenedor;
	 private JPanel pabajo;
	 private JPanel panelDetalle;
	 private Modelo modelo;
	 private FormaPagoVO formaPago;
	 
	 private JTextField nombre;
	 private JCheckBox cuenta;
	 
	 private JDialog principal;
	 
	 
	 PagePanel panelpdf;
	 PDFFile pdffile;
	 int indice = 0 ;
	 
	 /**
	  * Constructor por defecto.
	  */
	 public NuevoFormaPago(JDialog principal){
		 this.modelo = new Modelo();
		 this.principal = principal;
		 this.nombre=new JTextField(40);
		 this.cuenta=new JCheckBox("Cuenta bancaria",false);
		 this.formaPago=new FormaPagoVO();
		 
		 creaVentana();
		 
	 }
	 
	 
	 private void creaVentana(){
		 // Nueva ventana
		 ventana = new JDialog(principal,"Nueva Forma de pago",true);
		 ventana.addWindowListener(new cerrarWin());
		 ventana.setSize(300, 150);
		 ventana.setLocationRelativeTo(null);
		 ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		 ventana.setResizable(false);
		 
		 contenedor = ventana.getContentPane();
		 contenedor.setBackground(Color.WHITE);
		 contenedor.setLayout(new BorderLayout());
		 
		 // DATOS

		 panelDetalle = new JPanel();
		 panelDetalle.setLayout(new GridBagLayout());
		 panelDetalle.setBorder(new EmptyBorder(20,10,20,10));
		 GridBagConstraints ct = new GridBagConstraints();
		 ct.weightx = 1;
		 ct.gridx = 0;
		 ct.gridy = 0;
		 ct.weightx = 0;
		 ct.weighty = 0;
		 ct.fill = GridBagConstraints.HORIZONTAL;
		 panelDetalle.add(new JLabel("Nombre:"),ct);
		 ct.gridx++;
		 ct.weightx = 1;
		 panelDetalle.add(nombre,ct);
		 ct.weightx = 0;
		 ct.gridx = 0;
		 ct.gridy++;
		 ct.gridwidth = 2;
		 panelDetalle.add(cuenta,ct);


		 // BOTONES
		 pabajo = new JPanel();
		 pabajo.setLayout(new FlowLayout());

		 JButton bcerrar=new JButton("Cerrar");
		 bcerrar.addActionListener(new CerrarAL());
		 JButton bguardar=new JButton("Guardar");
		 bguardar.addActionListener(new GuardarAL());
		 
		 pabajo.add(bguardar);
		 pabajo.add(bcerrar);
	 
		 contenedor.add(panelDetalle,BorderLayout.CENTER);
		 contenedor.add(pabajo,BorderLayout.SOUTH);
		 ventana.setVisible(true);

	 }
	 
	 /**
	  * Devuelve el la forma de pago creada
	  * @return FormaPagoVO
	  */
	 public FormaPagoVO getFormaPago()
	 {
		 return formaPago;
	 }
	 
	 	 
	 
	 /**
	  * Escucha de botón guardar
	  * @author sergio
	  *
	  */
	 class GuardarAL implements ActionListener {
		 public void actionPerformed(ActionEvent evento){
				if (nombre.getText().equals("")){
					int result = JOptionPane.showConfirmDialog(ventana,
							"El nombre de la forma de pago es obligatorio", "¡Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					return;
				}
				if (modelo.nombrePagoDuplicado(nombre.getText())){
					int result = JOptionPane.showConfirmDialog(ventana,
							"Ya existe otra forma de pago con ese nombre", "¡Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					return;
				}
				formaPago.setNombre(nombre.getText());
				formaPago.setNumeroCuenta(cuenta.isSelected());
				formaPago.setIdFormaPago(modelo.nuevoIdFormaPago());
				modelo.guardarFormaPago(formaPago);
				ventana.dispose();
			}
	 }
	 
	 private void salir()
	 {
		 formaPago=null;
		 ventana.dispose();
	     principal.setEnabled(true);
		 return;
	 }
	 
	 class CerrarAL implements ActionListener {
		 public void actionPerformed(ActionEvent evento){
			 salir();
		 }
	 }

	 class cerrarWin extends WindowAdapter
	 {
	    public void windowClosing(WindowEvent e)
	   {
		    salir();
	   }
	 }
	 
	 
}